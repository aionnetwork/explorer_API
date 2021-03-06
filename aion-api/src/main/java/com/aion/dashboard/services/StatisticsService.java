package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.controllers.Dashboard;
import com.aion.dashboard.datatransferobject.HealthDTO;
import com.aion.dashboard.entities.AccountStats;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.Metrics;
import com.aion.dashboard.entities.Metrics.CompositeKey;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Statistics;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.entities.TransactionStats;
import com.aion.dashboard.entities.ValidatorStats;
import com.aion.dashboard.repositories.AccountsStatsJpaRepository;
import com.aion.dashboard.repositories.BlockJpaRepository;
import com.aion.dashboard.repositories.MetricsJpaRepository;
import com.aion.dashboard.repositories.ParserStateJpaRepository;
import com.aion.dashboard.repositories.TransactionJpaRepository;
import com.aion.dashboard.repositories.TransactionStatsJpaRepository;
import com.aion.dashboard.repositories.ValidatorStatsJPARepository;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.BackwardsCompactibilityUtil;
import com.aion.dashboard.utility.Logging;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatisticsService {

    private Dashboard dashboard;

    private BlockJpaRepository blkRepo;
    private AccountsStatsJpaRepository accountsStatsJpaRepository;
    private MetricsJpaRepository metRepo;
    private TransactionStatsJpaRepository transactionStatsJpaRepository;
    private TransactionJpaRepository txnRepo;
    private ValidatorStatsJPARepository validatorStatsJPARepository;

    private ParserStateJpaRepository pSRepo;
    private BlockService blockService;
    private TransactionService transactionService;
    private Logger logger = LoggerFactory.getLogger(Statistics.class);

    @Autowired
    public StatisticsService(Dashboard dashboard, BlockJpaRepository blkRepo,
        AccountsStatsJpaRepository accountsStatsJpaRepository,
        MetricsJpaRepository metRepo,
        TransactionStatsJpaRepository transactionStatsJpaRepository,
        TransactionJpaRepository txnRepo,
        ValidatorStatsJPARepository validatorStatsJPARepository,
        ParserStateJpaRepository pSRepo, BlockService blockService,
        TransactionService transactionService) {
        this.dashboard = dashboard;
        this.blkRepo = blkRepo;
        this.accountsStatsJpaRepository = accountsStatsJpaRepository;
        this.metRepo = metRepo;
        this.transactionStatsJpaRepository = transactionStatsJpaRepository;
        this.txnRepo = txnRepo;
        this.validatorStatsJPARepository = validatorStatsJPARepository;
        this.pSRepo = pSRepo;
        this.blockService = blockService;
        this.transactionService = transactionService;
    }

    private static final int SB_METRICS = 1;
    private static final int RT_METRICS = 2;

    private static final int DISPLAYED_BLKS = 4;
    private static final int DISPLAYED_TXNS = 10;
    private static final int PRECISION = 1;
    private static final long REALTIME_STATS_INTERVAL = 10_000;
    private static final long DASHBOARD_STATS_INTERVAL = 10_000;
    private static final long DAILY_ACCOUNT_STATS_INTERVAL = 3_600_000;

    private Metrics sbMetricsState;
    private Metrics rtMetricsState;

    @Cacheable(CacheConfig.BLOCK_NUMBER_3)
    public long lastStoredBlock(){
        return pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId())
            .map(ParserState::getBlockNumber)
            .orElseThrow();
    }

    @Cacheable(CacheConfig.BLOCK_NUMBER_VALIDATOR)
    public long lastStoredValidator(){
        return pSRepo.findById(ParserStateType.VALIDATOR_STATS.getId())
            .map(ParserState::getBlockNumber)
            .orElseThrow();
    }

    @Scheduled(fixedDelay = DASHBOARD_STATS_INTERVAL)
    public void calculateDashboardStatistics() {
        // Updates the Statistics Upon a New Block Received
        try {
            blocks();
            sbMetrics();
            transactions();
        } catch (Exception e) {

            Logging.traceException(e);
        }
    }

    @Scheduled(fixedDelay = DAILY_ACCOUNT_STATS_INTERVAL)
    public void calculateDailyAccountStatistics() {
        // Updates the Statistics ever Hour
        try {
            inboundTxns();
            outboundTxns();
        } catch (Exception e) {
            Logging.traceException(e);
        }
    }

    @Scheduled(fixedDelay = REALTIME_STATS_INTERVAL)
    public void calculateRealtimeStatistics() {
        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Optional<Metrics> metrics = metRepo.findById(new CompositeKey(RT_METRICS, lastStoredBlock()));

        // Confirms that there's no Reorg Occurring and the ParserState is Current
        if(metrics.isPresent() && parserState.isPresent()) {
            rtMetricsState = metrics.get();
            rtMetricsState.setTargetBlockTime(10L);
            rtMetricsState.setCurrentBlockchainHead(parserState.get().getBlockNumber());
            rtMetricsState.setBlockWindow();
        }

        // Populates Real Time Metrics based on the last saved Real Time Metrics State
        Statistics statistics = Statistics.getInstance();
        statistics.setRTMetrics(BackwardsCompactibilityUtil.Metrics(rtMetricsState));
    }

    public void sbMetrics() {
        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Optional<Metrics> metrics = metRepo.findById(new Metrics.CompositeKey(SB_METRICS, lastStoredBlock()));

        // Confirms that there's no Reorg Occurring and the ParserState is Current
        if(metrics.isPresent() && parserState.isPresent()) {
            sbMetricsState = metrics.get();
            sbMetricsState.setTargetBlockTime(10L);

            sbMetricsState.setCurrentBlockchainHead(0L);
            Optional<ParserState> lastBlockRead = pSRepo.findById(ParserStateType.HEAD_BLOCKCHAIN.getId());
            lastBlockRead.ifPresent(parserState1 -> sbMetricsState.setCurrentBlockchainHead(parserState1.getBlockNumber()));
            sbMetricsState.setBlockWindow();
        }

        // Populates Stable Metrics based on the last saved Stable Metrics State
        Statistics statistics = Statistics.getInstance();
        statistics.setSBMetrics(BackwardsCompactibilityUtil.Metrics(sbMetricsState));
    }

    public void blocks() throws Exception {
        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        if(parserState.isPresent()) {

            Page<Block> blksPage = blockService.findBlocks(0, DISPLAYED_BLKS);
            List<Block> blksList = blksPage.getContent();
            if (!blksList.isEmpty()) {
                JSONArray blocksArray = new JSONArray();
                for (Block block : blksList) {
                    blocksArray.put(new JSONObject(ow.writeValueAsString(block)));
                }

                Statistics statistics = Statistics.getInstance();
                statistics.setBlocks(blocksArray);
            }
        }
    }

    public void transactions() throws Exception {
        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Sort txnSort = new Sort(Sort.Direction.DESC, "blockTimestamp");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        if(!parserState.isPresent())
            throw new NullPointerException("ParserState is null");
        Page<Transaction> txnsPage = transactionService.findAll(0, DISPLAYED_TXNS);
        List<Transaction> txnsList = txnsPage.getContent();
        if(!txnsList.isEmpty()) {
            JSONArray transactionsArray = new JSONArray();
            for(Transaction transaction: txnsList) {
                JSONObject txObject = new JSONObject(ow.writeValueAsString(transaction));
                txObject.put("value", Utility.toAion(transaction.getValue()));
                transactionsArray.put(txObject);
            }

            Statistics statistics = Statistics.getInstance();
            statistics.setTransactions(transactionsArray);
        }
    }

    public void inboundTxns() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ParserState blockHead = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();

        Long currTime = blkRepo.findByBlockNumber(blockHead.getBlockNumber()).get().getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;
        List<Object> txnsList = txnRepo.findAvgsAndCountForToAddressByTimestampRange(oneDayAgo, currTime);

        if(!txnsList.isEmpty()) {

            JSONArray inboundTxns = new JSONArray();
            for(Object txn: txnsList) {

                // Parse out the Information
                String[] responseStrings = ow.writeValueAsString(txn)
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");

                JSONArray result = new JSONArray();
                result.put(responseStrings[0].trim());                                          // To Address
                result.put(responseStrings[1].trim());                                          // Average NRG Price
                result.put(responseStrings[2].trim());                                          // Average NRG Consumed
                result.put(responseStrings[3].trim());                                          // Count of transactions Found
                inboundTxns.put(result);
            }

            Statistics statistics = Statistics.getInstance();
            statistics.setInboundTxns(inboundTxns);
        }
    }

    public void outboundTxns() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ParserState blockHead = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();

        Long currTime = blkRepo.findByBlockNumber(blockHead.getBlockNumber()).get().getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;
        List<Object> txnsList = txnRepo.findAvgsAndCountForFromAddressByTimestampRange(oneDayAgo, currTime);

        if(!txnsList.isEmpty()) {

            JSONArray outboundTxns = new JSONArray();
            for(Object txn: txnsList) {

                // Parse out the Information
                String[] responseStrings = ow.writeValueAsString(txn)
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");

                JSONArray result = new JSONArray();
                result.put(responseStrings[0].trim());                                          // From Address
                result.put(responseStrings[1].trim());                                          // Average NRG Price
                result.put(responseStrings[2].trim());                                          // Average NRG Consumed
                result.put(responseStrings[3].trim());                                          // Count of transactions Found
                outboundTxns.put(result);
            }

            Statistics statistics = Statistics.getInstance();
            statistics.setOutboundTxns(outboundTxns);
        }
    }



    public HealthDTO health(){
        final long dbHead = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).orElseThrow().getBlockNumber();
        final long blockchainHead = pSRepo.findById(ParserStateType.HEAD_BLOCKCHAIN.getId()).orElseThrow().getBlockNumber();
        final long timeStamp = blkRepo.findByBlockNumber(dbHead).orElseThrow().getBlockTimestamp();

        return createHealthFrom(dbHead, blockchainHead, timeStamp);
    }

    static HealthDTO createHealthFrom(long dbHead, long blockchainHead, long timeStamp) {
        final String status;
        if (dbHead == blockchainHead){

            if ((System.currentTimeMillis()/1000 - 60*5) > timeStamp){
                status = "STALLED";
            }
            else {
                status = "OK";
            }
        }
        else {

            status = "SYNCING";
        }

        return new HealthDTO(dbHead, blockchainHead, timeStamp, status);
    }

    public Metrics getSbMetrics(){
        return getSbMetrics(lastStoredBlock());
    }

    public Metrics getRtMetrics(){
        return getRtMetrics(lastStoredBlock());
    }

    public Metrics getSbMetrics(long blockNumber){
        return metRepo.findById(new CompositeKey(SB_METRICS, blockNumber)).orElseThrow();
    }

    public Metrics getRtMetrics(long blockNumber){
        return metRepo.findById(new CompositeKey(RT_METRICS, blockNumber)).orElseThrow();
    }

    public Page<ValidatorStats> validatorStats(long blockNumber, int page, int size){
        if (blockNumber<360) {
            throw new NoSuchElementException();
        } else {
            Page<ValidatorStats> res;
            do {
                // find the last entry in the db that can be used
                long lastMetrics = blockNumber - blockNumber  % 360;
                res = validatorStatsJPARepository.findAllByBlockNumber(lastMetrics, PageRequest.of(page, size));
                blockNumber -= 360;
            }while (res.isEmpty() && blockNumber >= 360);
            return res;
        }
    }

    public Page<ValidatorStats> validatorStats(int page, int size){
        return validatorStats(lastStoredValidator(), page, size);
    }

    public Page<ValidatorStats> validatorStats(long blockNumber, String sealType, int page, int size){
        if (blockNumber<360 || blockNumber > lastStoredValidator()) {
            throw new NoSuchElementException();
        } else {
            Page<ValidatorStats> res;
            long metricsBlockNumber = blockNumber - blockNumber % 360;
            do {
                // find the last entry in the db that can be used
                res = validatorStatsJPARepository.findAllByBlockNumberAndSealType(metricsBlockNumber, sealType, PageRequest.of(page, size));
                metricsBlockNumber -= 360;
            }while (res.isEmpty() && metricsBlockNumber >= 360);
            return res;
        }
    }

    public Page<ValidatorStats> validatorStats(String sealType,int page, int size){
        return validatorStats(lastStoredBlock(), sealType, page, size);
    }

    public List<AccountStats> allAccountStats(){
        return accountsStatsJpaRepository.findAll();
    }

    public List<AccountStats> recentAccountStats(){
        return accountStats(pSRepo.findById(ParserStateType.TRANSACTION_STATS.getId()).orElseThrow().getBlockNumber());
    }

    public List<AccountStats> accountStats(long blockNumber){
        return accountsStatsJpaRepository.findByBlockNumber(blockNumber - (blockNumber%4320));
    }

    public List<TransactionStats> allTransactionStats(){
        return transactionStatsJpaRepository.findAll();
    }

    public TransactionStats transactionStats(long blockNumber){
        return transactionStatsJpaRepository.findById(blockNumber - (blockNumber%4320)).orElseThrow();
    }

    public TransactionStats transactionStats(){
        return transactionStats(pSRepo.findById(ParserStateType.TRANSACTION_STATS.getId()).orElseThrow().getBlockNumber());
    }
}

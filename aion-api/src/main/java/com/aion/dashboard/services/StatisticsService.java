package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.controllers.Dashboard;
import com.aion.dashboard.datatransferobject.HealthDTO;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.Metrics;
import com.aion.dashboard.entities.Metrics.CompositeKey;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Statistics;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repositories.BlockJpaRepository;
import com.aion.dashboard.repositories.MetricsJpaRepository;
import com.aion.dashboard.repositories.ParserStateJpaRepository;
import com.aion.dashboard.repositories.TransactionJpaRepository;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.BackwardsCompactibilityUtil;
import com.aion.dashboard.utility.Logging;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
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

    private MetricsJpaRepository metRepo;

    private TransactionJpaRepository txnRepo;


    private ParserStateJpaRepository pSRepo;

    @Autowired
    public StatisticsService(Dashboard dashboard, BlockJpaRepository blkRepo, MetricsJpaRepository metRepo, TransactionJpaRepository txnRepo, ParserStateJpaRepository pSRepo) {
        this.dashboard = dashboard;
        this.blkRepo = blkRepo;
        this.metRepo = metRepo;
        this.txnRepo = txnRepo;
        this.pSRepo = pSRepo;

    }

    private static final int SB_METRICS = 1;
    private static final int RT_METRICS = 2;

    private static final int DISPLAYED_BLKS = 4;
    private static final int DISPLAYED_TXNS = 10;
    private static final int PRECISION = 1;
    private static final long REALTIME_STATS_INTERVAL = 1000;
    private static final long DASHBOARD_STATS_INTERVAL = 2000;
    private static final long DAILY_ACCOUNT_STATS_INTERVAL = 3600000;

    private Metrics sbMetricsState;
    private Metrics rtMetricsState;

    @Cacheable(CacheConfig.LAST_STORED_BLOCK)
    public long lastStoredBlock(){
        return pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId())
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
            dashboard.newBlockDashboard();
        } catch (Exception e) {

            Logging.traceException(e);
        }
    }

    @Scheduled(fixedDelay = DAILY_ACCOUNT_STATS_INTERVAL)
    public void calculateDailyAccountStatistics() {
        // Updates the Statistics ever Hour
        try {
            minedBlks();
            inboundTxns();
            outboundTxns();
        } catch (Exception e) {
            Logging.traceException(e);
        }
    }

    @Scheduled(fixedDelay = REALTIME_STATS_INTERVAL)
    @Cacheable(CacheConfig.STATISTICS_RT_METRICS)
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

    @Cacheable(CacheConfig.STATISTICS_SB_METRICS)
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

    @Cacheable(CacheConfig.STATISTICS_BLOCKS)
    public void blocks() throws Exception {
        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Sort blkSort = new Sort(Sort.Direction.DESC, "blockNumber");

        if(parserState.isPresent()) {

            Instant instant = Instant.ofEpochSecond(blkRepo.findByBlockNumber(parserState.get().getBlockNumber()).get().getBlockTimestamp());
            int d = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getDayOfMonth();
            int m = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getMonthValue();
            int y = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getYear();

            Page<Block> blksPage = blkRepo.findByDayAndMonthAndYear(d, m, y, PageRequest.of(0, DISPLAYED_BLKS, blkSort));
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

    @Cacheable(CacheConfig.STATISTICS_TRANSACTIONS)
    public void transactions() throws Exception {
        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Sort txnSort = new Sort(Sort.Direction.DESC, "blockTimestamp");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        if(!parserState.isPresent())
            throw new NullPointerException("ParserState is null");

        Instant instant = Instant.ofEpochSecond(blkRepo.findByBlockNumber(parserState.get().getBlockNumber()).get().getBlockTimestamp());

        int d = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getDayOfMonth();
        int m = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getMonthValue();
        int y = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getYear();

        Page<Transaction> txnsPage = txnRepo.findByDayAndMonthAndYear(d, m, y, PageRequest.of(0, DISPLAYED_TXNS, txnSort));
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

    @Cacheable(CacheConfig.STATISTICS_MINED_BLOCKS)
    public void minedBlks() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        ParserState blockHead = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();
        Long currTime = blkRepo.findByBlockNumber(blockHead.getBlockNumber()).get().getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;
        List<Object> blksList = blkRepo.findAvgAndCountForAddressBetweenTimestamp(oneDayAgo, currTime);
        long numBlocks = blkRepo.countByBlockTimestampBetween(oneDayAgo, currTime);
        if(!blksList.isEmpty()) {

            JSONArray minedBlks = new JSONArray();
            for(Object blk: blksList) {

                // Parse out the Information
                String[] responseStrings = ow.writeValueAsString(blk)
                        .replace("\"", "")
                        .replace("[", "")
                        .replace("]", "")
                        .split(",");

                JSONArray result = new JSONArray();
                result.put(responseStrings[0].trim());                                              // Miner Address
                result.put(responseStrings[1].trim());                                              // Average Num of transactions
                result.put(new BigDecimal(responseStrings[2].trim())
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(numBlocks),PRECISION, RoundingMode.HALF_UP)
                        .toString());    // The Percentage of blocks Mined
                minedBlks.put(result);
            }

            Statistics statistics = Statistics.getInstance();
            statistics.setMinedBlks(minedBlks);
        }
    }

    @Cacheable(CacheConfig.STATISTICS_INBOUND_TRANSACTIONS)
    public void inboundTxns() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ParserState blockHead = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();

        Long currTime = blkRepo.findByBlockNumber(blockHead.getBlockNumber()).get().getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;


        var zdtStart = ZonedDateTime.ofInstant(Instant.ofEpochSecond(currTime), ZoneId.of("UTC"));
        var zdtEnd = ZonedDateTime.ofInstant(Instant.ofEpochSecond(oneDayAgo), ZoneId.of("UTC"));
        List<Object> txnsList = txnRepo.findAvgsAndCountForToAddressByTimestampRange(oneDayAgo, currTime, zdtStart.getYear(), zdtStart.getMonthValue(), zdtEnd.getYear(), zdtEnd.getMonthValue());

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

    @Cacheable(CacheConfig.STATISTICS_OUTBOUND_TRANSACTIONS)
    public void outboundTxns() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ParserState blockHead = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();

        Long currTime = blkRepo.findByBlockNumber(blockHead.getBlockNumber()).get().getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;
        var zdtStart = ZonedDateTime.ofInstant(Instant.ofEpochSecond(currTime), ZoneId.of("UTC"));
        var zdtEnd = ZonedDateTime.ofInstant(Instant.ofEpochSecond(oneDayAgo), ZoneId.of("UTC"));
        List<Object> txnsList = txnRepo.findAvgsAndCountForFromAddressByTimestampRange(oneDayAgo, currTime, zdtStart.getYear(), zdtStart.getMonthValue(), zdtEnd.getYear(), zdtEnd.getMonthValue());

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
}

package com.aion.dashboard.services.mysql;

import com.aion.dashboard.controllers.Dashboard;
import com.aion.dashboard.entities.*;
import com.aion.dashboard.services.*;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.BackwardsCompactibilityUtil;
import com.aion.dashboard.utility.Logging;
import com.aion.dashboard.utility.RewardsCalculator;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class StatisticsImpMysql implements StatisticsService {

    @Autowired
    private Dashboard dashboard;

    @Autowired
    private BlockService blockService;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ParserStateService parserStateService;

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

    @Scheduled(fixedDelay = DASHBOARD_STATS_INTERVAL)
    private void calculateDashboardStatistics() {
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
    private void calculateDailyAccountStatistics() {
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
    public void calculateRealtimeStatistics() {
        Optional<ParserState> parserState = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Optional<Metrics> metrics = metricsService.findById(RT_METRICS);

        // Confirms that there's no Reorg Occurring and the ParserState is Current
        if(metrics.isPresent() && parserState.isPresent()) {
            rtMetricsState = metrics.get();
            rtMetricsState.setTargetBlockTime(10L);
            rtMetricsState.setCurrentBlockchainHead(parserState.get().getBlockNumber());
            rtMetricsState.setLastBlockReward();
            rtMetricsState.setBlockWindow();
        }

        // Populates Real Time Metrics based on the last saved Real Time Metrics State
        Statistics statistics = Statistics.getInstance();
        statistics.setRTMetrics(BackwardsCompactibilityUtil.Metrics(rtMetricsState));
    }


    public void sbMetrics() {
        Optional<ParserState> parserState = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Optional<Metrics> metrics = metricsService.findById(SB_METRICS);

        // Confirms that there's no Reorg Occurring and the ParserState is Current
        if(metrics.isPresent() && parserState.isPresent()) {
            sbMetricsState = metrics.get();
            sbMetricsState.setTargetBlockTime(10L);

            sbMetricsState.setCurrentBlockchainHead(0L);
            Optional<ParserState> lastBlockRead = parserStateService.findById(ParserStateType.HEAD_BLOCKCHAIN.getId());
            lastBlockRead.ifPresent(parserState1 -> sbMetricsState.setCurrentBlockchainHead(parserState1.getBlockNumber()));

            sbMetricsState.setLastBlockReward();
            sbMetricsState.setBlockWindow();
        }

        // Populates Stable Metrics based on the last saved Stable Metrics State
        Statistics statistics = Statistics.getInstance();
        statistics.setSBMetrics(BackwardsCompactibilityUtil.Metrics(sbMetricsState));
    }


    private  void blocks() throws Exception {
        Optional<ParserState> parserState = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Sort blkSort = new Sort(Sort.Direction.DESC, "blockNumber");

        if(parserState.isPresent()) {

            Instant instant = Instant.ofEpochSecond(blockService.findByBlockNumber(parserState.get().getBlockNumber()).getBlockTimestamp());
            int d = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getDayOfMonth();
            int m = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getMonthValue();
            int y = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getYear();

            Page<Block> blksPage = blockService.findByDayAndMonthAndYear(d, m, y, PageRequest.of(0, DISPLAYED_BLKS, blkSort));
            List<Block> blksList = blksPage.getContent();
            if (!blksList.isEmpty()) {
                JSONArray blocksArray = new JSONArray();
                for (Block block : blksList) {
                    block.setBlockReward(RewardsCalculator.calculateReward(block.getBlockNumber()));
                    blocksArray.put(new JSONObject(ow.writeValueAsString(block)));
                }

                Statistics statistics = Statistics.getInstance();
                statistics.setBlocks(blocksArray);
            }
        }
    }


    private void transactions() throws Exception {
        Optional<ParserState> parserState = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        Sort txnSort = new Sort(Sort.Direction.DESC, "blockTimestamp");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        if(!parserState.isPresent())
            throw new NullPointerException("ParserState is null");

        Instant instant = Instant.ofEpochSecond(blockService.findByBlockNumber(parserState.get().getBlockNumber()).getBlockTimestamp());

        int d = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getDayOfMonth();
        int m = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getMonthValue();
        int y = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC")).getYear();

        Page<Transaction> txnsPage = transactionService.findByDayAndMonthAndYear(d, m, y, PageRequest.of(0, DISPLAYED_TXNS, txnSort));
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

    private void minedBlks() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        ParserState blockHead = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();
        Long currTime = blockService.findByBlockNumber(blockHead.getBlockNumber()).getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;
        List<Object> blksList = blockService.findAvgAndCountForAddressBetweenTimestamp(oneDayAgo, currTime);
        long numBlocks = blockService.countByBlockTimestampBetween(oneDayAgo, currTime);
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

    private void inboundTxns() throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ParserState blockHead = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();

        Long currTime = blockService.findByBlockNumber(blockHead.getBlockNumber()).getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;


        var zdtStart = ZonedDateTime.ofInstant(Instant.ofEpochSecond(currTime), ZoneId.of("UTC"));
        var zdtEnd = ZonedDateTime.ofInstant(Instant.ofEpochSecond(oneDayAgo), ZoneId.of("UTC"));
        List<Object> txnsList = transactionService.findAvgsAndCountForToAddressByTimestampRange(oneDayAgo, currTime, zdtStart.getYear(), zdtStart.getMonthValue(), zdtEnd.getYear(), zdtEnd.getMonthValue());

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
        ParserState blockHead = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId()).get();

        Long currTime = blockService.findByBlockNumber(blockHead.getBlockNumber()).getBlockTimestamp();
        Long oneDayAgo = currTime - 86400;
        var zdtStart = ZonedDateTime.ofInstant(Instant.ofEpochSecond(currTime), ZoneId.of("UTC"));
        var zdtEnd = ZonedDateTime.ofInstant(Instant.ofEpochSecond(oneDayAgo), ZoneId.of("UTC"));
        List<Object> txnsList = transactionService.findAvgsAndCountForFromAddressByTimestampRange(oneDayAgo, currTime, zdtStart.getYear(), zdtStart.getMonthValue(), zdtEnd.getYear(), zdtEnd.getMonthValue());

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
}

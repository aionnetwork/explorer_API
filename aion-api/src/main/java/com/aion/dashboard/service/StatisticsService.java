package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.controller.Dashboard;
import com.aion.dashboard.entities.*;
import com.aion.dashboard.repository.BlockJpaRepository;
import com.aion.dashboard.repository.MetricsJpaRepository;
import com.aion.dashboard.repository.ParserStateJpaRepository;
import com.aion.dashboard.repository.TransactionJpaRepository;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.BackwardsCompactibilityUtil;
import com.aion.dashboard.utility.RewardsCalculator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StatisticsService {

    @Autowired
    private Dashboard dashboard;

    @Autowired
    private BlockJpaRepository blockJpaRepository;

    @Autowired
    private MetricsJpaRepository metricsJpaRepository;

    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private ParserStateJpaRepository parserStateJpaRepository;

    private final int SB_METRICS = 1;
    private final int RT_METRICS = 2;

    private final int DISPLAYED_BLKS = 5;
    private final int DISPLAYED_TXNS = 10;

    private final long REALTIME_STATS_INTERVAL = 1000;
    private final long DASHBOARD_STATS_INTERVAL = 2000;

    private static Metrics SB_METRICS_STATE;
    private static Metrics RT_METRICS_STATE;

    private static List<Block> BLOCKS_STATE = new ArrayList<>();
    private static List<Transaction> TRANSACTIONS_STATE = new ArrayList<>();

    /*@Scheduled(fixedDelay = DASHBOARD_STATS_INTERVAL)
    public void calculateDashboardStatistics() {
        Optional<ParserState> lastBlockRead = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
		if(lastBlockRead.isPresent()) {
            Long blockNumber = lastBlockRead.get().getBlockNumber();

            // Updates the Statistics Upon a New Block Received
            try {
                Blocks();
                SBMetrics();
                Transactions();
                dashboard.newBlockDashboard();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

//    @Scheduled(fixedDelay = REALTIME_STATS_INTERVAL)
//    @Cacheable(CacheConfig.STATISTICS_RT_METRICS)
//    public void RTMetrics() {
//        try {
//            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
//            Optional<Metrics> metrics = metricsJpaRepository.findById(RT_METRICS);
//
//            // Confirms that there's no Reorg Occurring and the ParserState is Current
//            if(metrics.isPresent() && parserState.isPresent()) {
//                RT_METRICS_STATE = metrics.get();
//                RT_METRICS_STATE.setTargetBlockTime(10L);
//                RT_METRICS_STATE.setCurrentBlockchainHead(parserState.get().getBlockNumber());
//                RT_METRICS_STATE.setLastBlockReward();
//                RT_METRICS_STATE.setBlockWindow();
//            }
//
//            // Populates Real Time Metrics based on the last saved Real Time Metrics State
//            Statistics statistics = Statistics.getInstance();
//            statistics.setRTMetrics(BackwardsCompactibilityUtil.Metrics(RT_METRICS_STATE));
//        } catch(Exception e) {
//            throw e;
//        }
//    }

    @Cacheable(CacheConfig.STATISTICS_SB_METRICS)
    public void SBMetrics() {
        try {
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
            Optional<Metrics> metrics = metricsJpaRepository.findById(SB_METRICS);

            // Confirms that there's no Reorg Occurring and the ParserState is Current
            if(metrics.isPresent() && parserState.isPresent()) {
                SB_METRICS_STATE = metrics.get();
                SB_METRICS_STATE.setTargetBlockTime(10L);
                SB_METRICS_STATE.setCurrentBlockchainHead(parserState.get().getBlockNumber());
                SB_METRICS_STATE.setLastBlockReward();
                SB_METRICS_STATE.setBlockWindow();
            }

            // Populates Stable Metrics based on the last saved Stable Metrics State
            Statistics statistics = Statistics.getInstance();
            statistics.setSBMetrics(BackwardsCompactibilityUtil.Metrics(SB_METRICS_STATE));
        } catch(Exception e) {
            throw e;
        }
    }

    @Cacheable(CacheConfig.STATISTICS_BLOCKS)
    public void Blocks() throws Exception {
        try {
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Optional<Metrics> metrics = metricsJpaRepository.findById(SB_METRICS);

            // Confirms that there's no Reorg Occurring and the ParserState is Current
            if(metrics.isPresent() && parserState.isPresent()) {
                Sort blkSort = new Sort(Sort.Direction.DESC, "blockNumber");

                // Flush the Stable Blocks State before Adding to it
                BLOCKS_STATE = new ArrayList<>();

                // Calculating the Block Window and Current Blockchain Head of the Metrics
                metrics.get().setCurrentBlockchainHead(parserState.get().getBlockNumber());
                metrics.get().setBlockWindow();

                // Get all the Blocks within the Last Hour and Store the Last 5 to be Displayed
                long end = parserState.get().getBlockNumber();
                long start = end - DISPLAYED_BLKS;
                Page<Block> blocksPage = blockJpaRepository.findByBlockNumberBetween(start, end, new PageRequest(0, DISPLAYED_BLKS, blkSort));
                if(blocksPage != null) {
                    List<Block> blocksList = blocksPage.getContent();
                    if(blocksList != null && blocksList.size() > 0) {
                        for(Block block: blocksList) {
                            block.setBlockReward(RewardsCalculator.calculateReward(block.getBlockNumber()));
                            BLOCKS_STATE.add(block);
                        }
                    }
                }
            }

            // Populates Stable Blocks based on the last saved Stable Blocks State
            JSONArray blocksArray = new JSONArray();
            for(Block block: BLOCKS_STATE) {
                blocksArray.put(new JSONObject(ow.writeValueAsString(block)));
            }

            Statistics statistics = Statistics.getInstance();
            statistics.setBlocks(blocksArray);
        } catch (Exception e) {
            throw e;
        }
    }

    @Cacheable(CacheConfig.STATISTICS_TRANSACTIONS)
    public void Transactions() throws Exception {
        try {
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            Optional<Metrics> metrics = metricsJpaRepository.findById(SB_METRICS);

            // Confirms that there's no Reorg Occurring and the ParserState is Current
            if(metrics.isPresent() && parserState.isPresent()) {
                Sort txnSort = new Sort(Sort.Direction.DESC, "id");

                // Flush the Stable Transactions State before Adding to it
                TRANSACTIONS_STATE = new ArrayList<>();

                // Gets all the Transactions within the Last Hour and Store the Last 10 to be Displayed
                long end = parserState.get().getTransactionId();
                long start = end - DISPLAYED_TXNS;
                Page<Transaction> transactionsPage = transactionJpaRepository.findByIdBetween(start, end, new PageRequest(0, DISPLAYED_TXNS, txnSort));
                if(transactionsPage != null) {
                    List<Transaction> transactionsList = transactionsPage.getContent();
                    if(transactionsList != null && transactionsList.size() > 0) {
                        for(Transaction transaction: transactionsList) {
                            TRANSACTIONS_STATE.add(transaction);
                        }
                    }
                }
            }

            // Populates Stable Transactions based on the last saved Stable Transactions State
            JSONArray transactionsArray = new JSONArray();
            for(Transaction transaction: TRANSACTIONS_STATE) {
                transactionsArray.put(new JSONObject(ow.writeValueAsString(transaction)));
            }

            Statistics statistics = Statistics.getInstance();
            statistics.setTransactions(transactionsArray);
        } catch(Exception e) {
            throw e;
        }
    }
}

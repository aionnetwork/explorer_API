package com.aion.dashboard.utility;

import com.aion.dashboard.entities.Metrics;
import org.json.JSONObject;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BackwardsCompactibilityUtil {
    public static JSONObject Metrics(Metrics metrics) {
        JSONObject metricsObject = new JSONObject();

        metricsObject.put("totalTransactionsInLast24hours", metrics.getTotalTransaction());
        metricsObject.put("transactionPerSecond", metrics.getTransactionsPerSecond());
        metricsObject.put("blockWindow", metrics.getBlockWindow());
        metricsObject.put("peakTransactionsPerBlockInLast24hours", metrics.getPeakTransactionsPerBlock());
        metricsObject.put("endBlock", metrics.getEndBlock());
        metricsObject.put("lastBlockReward", metrics.getLastBlockReward());
        metricsObject.put("averageNrgLimitPerBlock", metrics.getAverageNrgLimit());
        metricsObject.put("startBlock", metrics.getStartBlock());
        metricsObject.put("targetBlockTime", metrics.getTargetBlockTime());
        metricsObject.put("currentBlockchainHead", metrics.getCurrentBlockchainHead());
        metricsObject.put("averageNrgConsumedPerBlock", metrics.getAverageNrgConsumed());
        metricsObject.put("hashRate", metrics.getAveragedHashPower());
        metricsObject.put("averageBlockTime", metrics.getAveragedBlockTime());
        metricsObject.put("averageDifficulty", metrics.getAverageDifficulty());
        metricsObject.put("endTimestamp", metrics.getEndTimestamp());
        metricsObject.put("startTimestamp", metrics.getStartTimestamp());

        return metricsObject;
    }

    public static class oldMetrics {
        Integer peakTransactionsPerBlockInLast24hours;
        BigInteger totalTransactionsInLast24hours;
        BigInteger lastBlockReward;
        BigInteger startBlock;
        BigInteger endBlock;
        BigDecimal averageNrgConsumedPerBlock;
        BigDecimal averageNrgLimitPerBlock;
        BigDecimal transactionPerSecond;
        BigDecimal averageDifficulty;
        BigDecimal averageBlockTime;
        BigDecimal hashRate;
        Long currentBlockchainHead;
        Long targetBlockTime;
        Long startTimestamp;
        Long endTimestamp;
        Long blockWindow;

        public Integer getPeakTransactionsPerBlockInLast24hours() {
            return peakTransactionsPerBlockInLast24hours;
        }

        public BigInteger getTotalTransactionsInLast24hours() {
            return totalTransactionsInLast24hours;
        }
        public BigInteger getLastBlockReward() {
            return lastBlockReward;
        }
        public BigInteger getStartBlock() {
            return startBlock;
        }
        public BigInteger getEndBlock() {
            return endBlock;
        }

        public BigDecimal getAverageNrgConsumedPerBlock() {
            return averageNrgConsumedPerBlock;
        }
        public BigDecimal getAverageNrgLimitPerBlock() {
            return averageNrgLimitPerBlock;
        }
        public BigDecimal getTransactionPerSecond() {
            return transactionPerSecond;
        }
        public BigDecimal getAverageDifficulty() {
            return averageDifficulty;
        }
        public BigDecimal getAverageBlockTime() {
            return averageBlockTime;
        }
        public BigDecimal getHashRate() {
            return hashRate;
        }

        public Long getCurrentBlockchainHead() {
            return currentBlockchainHead;
        }
        public Long getTargetBlockTime() {
            return targetBlockTime;
        }
        public Long getStartTimestamp() {
            return startTimestamp;
        }
        public Long getEndTimestamp() {
            return endTimestamp;
        }
        public Long getBlockWindow() {
            return blockWindow;
        }

        public void setPeakTransactionsPerBlockInLast24hours(Integer peakTransactionsPerBlockInLast24hours) {
            this.peakTransactionsPerBlockInLast24hours = peakTransactionsPerBlockInLast24hours;
        }

        public void setTotalTransactionsInLast24hours(BigInteger totalTransactionsInLast24hours) {
            this.totalTransactionsInLast24hours = totalTransactionsInLast24hours;
        }
        public void setLastBlockReward(BigInteger lastBlockReward) {
            this.lastBlockReward = lastBlockReward;
        }
        public void setStartBlock(BigInteger startBlock) {
            this.startBlock = startBlock;
        }
        public void setEndBlock(BigInteger endBlock) {
            this.endBlock = endBlock;
        }

        public void setAverageNrgConsumedPerBlock(BigDecimal averageNrgConsumedPerBlock) {
            this.averageNrgConsumedPerBlock = averageNrgConsumedPerBlock;
        }
        public void setAverageNrgLimitPerBlock(BigDecimal averageNrgLimitPerBlock) {
            this.averageNrgLimitPerBlock = averageNrgLimitPerBlock;
        }
        public void setTransactionPerSecond(BigDecimal transactionPerSecond) {
            this.transactionPerSecond = transactionPerSecond;
        }
        public void setAverageDifficulty(BigDecimal averageDifficulty) {
            this.averageDifficulty = averageDifficulty;
        }
        public void setAverageBlockTime(BigDecimal averageBlockTime) {
            this.averageBlockTime = averageBlockTime;
        }
        public void setHashRate(BigDecimal hashRate) {
            this.hashRate = hashRate;
        }

        public void setCurrentBlockchainHead(Long currentBlockchainHead) {
            this.currentBlockchainHead = currentBlockchainHead;
        }
        public void setTargetBlockTime(Long targetBlockTime) {
            this.targetBlockTime = targetBlockTime;
        }
        public void setStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
        }
        public void setEndTimestamp(Long endTimestamp) {
            this.endTimestamp = endTimestamp;
        }
        public void setBlockWindow(Long blockWindow) {
            this.blockWindow = blockWindow;
        }

        public static oldMetrics toObject(JSONObject jsonObject) {
            oldMetrics oldMetrics = new oldMetrics();

            oldMetrics.setPeakTransactionsPerBlockInLast24hours(Integer.valueOf(jsonObject.getString("peakTransactionsPerBlockInLast24hours")));

            oldMetrics.setTotalTransactionsInLast24hours(BigInteger.valueOf(jsonObject.getLong("totalTransactionsInLast24hours")));
            oldMetrics.setLastBlockReward(BigInteger.valueOf(jsonObject.getLong("lastBlockReward")));
            oldMetrics.setStartBlock(BigInteger.valueOf(jsonObject.getLong("startBlock")));
            oldMetrics.setEndBlock(BigInteger.valueOf(jsonObject.getLong("endBlock")));

            oldMetrics.setAverageNrgConsumedPerBlock(BigDecimal.valueOf(jsonObject.getDouble("averageNrgLimitPerBlock")));
            oldMetrics.setAverageNrgLimitPerBlock(BigDecimal.valueOf(jsonObject.getDouble("averageNrgLimitPerBlock")));
            oldMetrics.setTransactionPerSecond(BigDecimal.valueOf(jsonObject.getDouble("transactionPerSecond")));
            oldMetrics.setAverageDifficulty(BigDecimal.valueOf(jsonObject.getDouble("averageDifficulty")));
            oldMetrics.setAverageBlockTime(BigDecimal.valueOf(jsonObject.getDouble("averageBlockTime")));
            oldMetrics.setHashRate(BigDecimal.valueOf(jsonObject.getDouble("averageBlockTime")));

            oldMetrics.setCurrentBlockchainHead(jsonObject.getLong("currentBlockchainHead"));
            oldMetrics.setTargetBlockTime(jsonObject.getLong("targetBlockTime"));
            oldMetrics.setStartTimestamp(jsonObject.getLong("startTimestamp"));
            oldMetrics.setEndTimestamp(jsonObject.getLong("endTimestamp"));
            oldMetrics.setBlockWindow(jsonObject.getLong("blockWindow"));

            return oldMetrics;
        }
    }
}

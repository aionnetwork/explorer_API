package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MetricsDTO {
    private Integer id;
    private BigInteger totalTransaction;
    private BigDecimal transactionsPerSecond;
    private Integer peakTransactionsPerBlock;
    private BigInteger startBlock;
    private BigInteger endBlock;
    private BigDecimal averageNrgConsumed;
    private BigDecimal averageNrgLimit;
    private BigDecimal averagedBlockTime;
    private BigDecimal averageDifficulty;
    private Long endTimestamp;
    private Long startTimestamp;
    private BigDecimal averagedHashPower;
    private BigDecimal lastBlockReward;
    private Long blockWindow;
    private Long targetBlockTime;
    private Long currentBlockchainHead;

    public MetricsDTO(Integer id, BigInteger totalTransaction, BigDecimal transactionsPerSecond, Integer peakTransactionsPerBlock, BigInteger startBlock, BigInteger endBlock, BigDecimal averageNrgConsumed, BigDecimal averageNrgLimit, BigDecimal averagedBlockTime, BigDecimal averageDifficulty, Long endTimestamp, Long startTimestamp, BigDecimal averagedHashPower, BigDecimal lastBlockReward, Long currentBlockchainHead) {
        this.id = id;
        this.totalTransaction = totalTransaction;
        this.transactionsPerSecond = transactionsPerSecond;
        this.peakTransactionsPerBlock = peakTransactionsPerBlock;
        this.startBlock = startBlock;
        this.endBlock = endBlock;
        this.averageNrgConsumed = averageNrgConsumed;
        this.averageNrgLimit = averageNrgLimit;
        this.averagedBlockTime = averagedBlockTime;
        this.averageDifficulty = averageDifficulty;
        this.endTimestamp = endTimestamp;
        this.startTimestamp = startTimestamp;
        this.averagedHashPower = averagedHashPower;
        this.lastBlockReward = lastBlockReward;
        this.blockWindow = endBlock.subtract(startBlock).abs().longValue();
        this.targetBlockTime = 10L;
        this.currentBlockchainHead = currentBlockchainHead;
    }

    public String getId() {
        return id == 1 ? "stable" : "rt";
    }

    public BigInteger getTotalTransaction() {
        return totalTransaction;
    }

    public BigDecimal getTransactionsPerSecond() {
        return transactionsPerSecond;
    }

    public Integer getPeakTransactionsPerBlock() {
        return peakTransactionsPerBlock;
    }

    public BigInteger getStartBlock() {
        return startBlock;
    }

    public BigInteger getEndBlock() {
        return endBlock;
    }

    public BigDecimal getAverageNrgConsumed() {
        return averageNrgConsumed;
    }

    public BigDecimal getAverageNrgLimit() {
        return averageNrgLimit;
    }

    public BigDecimal getAveragedBlockTime() {
        return averagedBlockTime;
    }

    public BigDecimal getAverageDifficulty() {
        return averageDifficulty;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public BigDecimal getAveragedHashPower() {
        return averagedHashPower;
    }

    public BigDecimal getLastBlockReward() {
        return lastBlockReward;
    }

    public Long getBlockWindow() {
        return blockWindow;
    }

    public Long getTargetBlockTime() {
        return targetBlockTime;
    }

    public Long getCurrentBlockchainHead() {
        return currentBlockchainHead;
    }

    public static class MetricsDTOBuilder {
        private Integer id;
        private BigInteger totalTransaction;
        private BigDecimal transactionsPerSecond;
        private Integer peakTransactionsPerBlock;
        private BigInteger startBlock;
        private BigInteger endBlock;
        private BigDecimal averageNrgConsumed;
        private BigDecimal averageNrgLimit;
        private BigDecimal averagedBlockTime;
        private BigDecimal averageDifficulty;
        private Long endTimestamp;
        private Long startTimestamp;
        private BigDecimal averagedHashPower;
        private BigDecimal lastBlockReward;
        private Long currentBlockchainHead;

        public MetricsDTOBuilder setId(Integer id) {
            this.id = id;
            return this;
        }

        public MetricsDTOBuilder setTotalTransaction(BigInteger totalTransaction) {
            this.totalTransaction = totalTransaction;
            return this;
        }

        public MetricsDTOBuilder setTransactionsPerSecond(BigDecimal transactionsPerSecond) {
            this.transactionsPerSecond = transactionsPerSecond;
            return this;
        }

        public MetricsDTOBuilder setPeakTransactionsPerBlock(Integer peakTransactionsPerBlock) {
            this.peakTransactionsPerBlock = peakTransactionsPerBlock;
            return this;
        }

        public MetricsDTOBuilder setStartBlock(BigInteger startBlock) {
            this.startBlock = startBlock;
            return this;
        }

        public MetricsDTOBuilder setEndBlock(BigInteger endBlock) {
            this.endBlock = endBlock;
            return this;
        }

        public MetricsDTOBuilder setAverageNrgConsumed(BigDecimal averageNrgConsumed) {
            this.averageNrgConsumed = averageNrgConsumed;
            return this;
        }

        public MetricsDTOBuilder setAverageNrgLimit(BigDecimal averageNrgLimit) {
            this.averageNrgLimit = averageNrgLimit;
            return this;
        }

        public MetricsDTOBuilder setAveragedBlockTime(BigDecimal averagedBlockTime) {
            this.averagedBlockTime = averagedBlockTime;
            return this;
        }

        public MetricsDTOBuilder setAverageDifficulty(BigDecimal averageDifficulty) {
            this.averageDifficulty = averageDifficulty;
            return this;
        }

        public MetricsDTOBuilder setEndTimestamp(Long endTimestamp) {
            this.endTimestamp = endTimestamp;
            return this;
        }

        public MetricsDTOBuilder setStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public MetricsDTOBuilder setAveragedHashPower(BigDecimal averagedHashPower) {
            this.averagedHashPower = averagedHashPower;
            return this;
        }

        public MetricsDTOBuilder setLastBlockReward(BigDecimal lastBlockReward) {
            this.lastBlockReward = lastBlockReward;
            return this;
        }


        public MetricsDTOBuilder setCurrentBlockchainHead(Long currentBlockchainHead) {
            this.currentBlockchainHead = currentBlockchainHead;
            return this;
        }

        public MetricsDTO buildMetricsDTO() {
            return new MetricsDTO(
                    id,
                    totalTransaction,
                    transactionsPerSecond,
                    peakTransactionsPerBlock,
                    startBlock,
                    endBlock,
                    averageNrgConsumed,
                    averageNrgLimit,
                    averagedBlockTime,
                    averageDifficulty,
                    endTimestamp,
                    startTimestamp,
                    averagedHashPower,
                    lastBlockReward,
                    currentBlockchainHead
            );
        }
    }
}

package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unused")
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
    private BigDecimal powBlockDifficulty;
    private BigDecimal posBlockDifficulty;
    private BigDecimal powBlockTime;
    private BigDecimal posBlockTime;
    private BigDecimal averagePosIssuance;
    private BigDecimal percentageOfNetworkStaking;
    private BigDecimal totalStake;

    public MetricsDTO(MetricsDTOBuilder builder) {
        this.id = builder.id;
        this.totalTransaction = builder.totalTransaction;
        this.transactionsPerSecond = builder.transactionsPerSecond;
        this.peakTransactionsPerBlock = builder.peakTransactionsPerBlock;
        this.startBlock = builder.startBlock;
        this.endBlock = builder.endBlock;
        this.averageNrgConsumed = builder.averageNrgConsumed;
        this.averageNrgLimit = builder.averageNrgLimit;
        this.averagedBlockTime = builder.averagedBlockTime;
        this.averageDifficulty = builder.averageDifficulty;
        this.endTimestamp = builder.endTimestamp;
        this.startTimestamp = builder.startTimestamp;
        this.averagedHashPower = builder.averagedHashPower;
        this.lastBlockReward = builder.lastBlockReward;
        this.blockWindow = endBlock.subtract(startBlock).abs().longValue();
        this.targetBlockTime = 10L;
        this.currentBlockchainHead = builder.currentBlockchainHead;
        this.posBlockDifficulty = builder.posBlockDifficulty;
        this.powBlockDifficulty = builder.powBlockDifficulty;
        this.powBlockTime = builder.powBlockTime;
        this.posBlockTime = builder.posBlockTime;
        this.averagePosIssuance = builder.averagePosIssuance;
        this.percentageOfNetworkStaking = builder.percentageOfNetworkStaking;
        this.totalStake = builder.totalStake;
    }

    public BigDecimal getPowBlockDifficulty() {
        return powBlockDifficulty;
    }

    public BigDecimal getPosBlockDifficulty() {
        return posBlockDifficulty;
    }

    public BigDecimal getPowBlockTime() {
        return powBlockTime;
    }

    public BigDecimal getPosBlockTime() {
        return posBlockTime;
    }

    public BigDecimal getAveragePosIssuance() {
        return averagePosIssuance;
    }

    public BigDecimal getPercentageOfNetworkStaking() {
        return percentageOfNetworkStaking;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
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
        private BigDecimal powBlockDifficulty;
        private BigDecimal posBlockDifficulty;
        private BigDecimal powBlockTime;
        private BigDecimal posBlockTime;
        private BigDecimal averagePosIssuance;
        private BigDecimal percentageOfNetworkStaking;
        private BigDecimal totalStake;

        public MetricsDTOBuilder setPowBlockDifficulty(BigDecimal powBlockDifficulty) {
            this.powBlockDifficulty = powBlockDifficulty;
            return this;
        }

        public MetricsDTOBuilder setPosBlockDifficulty(BigDecimal posBlockDifficulty) {
            this.posBlockDifficulty = posBlockDifficulty;
            return this;
        }

        public MetricsDTOBuilder setPowBlockTime(BigDecimal powBlockTime) {
            this.powBlockTime = powBlockTime;
            return this;
        }

        public MetricsDTOBuilder setPosBlockTime(BigDecimal posBlockTime) {
            this.posBlockTime = posBlockTime;
            return this;
        }

        public MetricsDTOBuilder setAveragePosIssuance(BigDecimal averagePosIssuance) {
            this.averagePosIssuance = averagePosIssuance;
            return this;
        }

        public MetricsDTOBuilder setPercentageOfNetworkStaking(
            BigDecimal percentageOfNetworkStaking) {
            this.percentageOfNetworkStaking = percentageOfNetworkStaking;
            return this;
        }

        public MetricsDTOBuilder setTotalStake(BigDecimal totalStake) {
            this.totalStake = totalStake;
            return this;
        }

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
            return new MetricsDTO(this);
        }
    }
}

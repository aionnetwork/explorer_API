package com.aion.dashboard.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "Metrics")
@IdClass(Metrics.CompositeKey.class)
public class Metrics {

    @Id private Integer id;
    private BigInteger totalTransaction;
    private BigDecimal transactionsPerSecond;
    private Integer peakTransactionsPerBlock;
    private BigInteger startBlock;
    @Id private BigInteger endBlock;
    private BigDecimal averageNrgConsumed;
    private BigDecimal averageNrgLimit;
    private BigDecimal averagedBlockTime;
    private BigDecimal averageDifficulty;
    private Long endTimestamp;
    private Long startTimestamp;
    private BigDecimal averagedHashPower;
    private BigDecimal lastBlockReward;
    @Transient
    private Long blockWindow;
    @Transient
    private Long targetBlockTime = 10L;
    @Transient
    private Long currentBlockchainHead;
    private BigDecimal powAvgDifficulty;
    private BigDecimal posAvgDifficulty;
    private BigDecimal powAvgBlockTime;
    private BigDecimal posAvgBlockTime;
    private BigDecimal avgPosIssuance;
    private BigDecimal percentageOfNetworkStaking;
    private BigDecimal totalStake;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAveragedHashPower() {
        return averagedHashPower;
    }

    public void setAveragedHashPower(BigDecimal averagedHashPower) {
        this.averagedHashPower = averagedHashPower;
    }

    public BigDecimal getAveragedBlockTime() {
        return averagedBlockTime;
    }

    public void setAveragedBlockTime(BigDecimal averagedBlockTime) {
        this.averagedBlockTime = averagedBlockTime;
    }

    public BigDecimal getAverageDifficulty() {
        return averageDifficulty;
    }

    public void setAverageDifficulty(BigDecimal averageDifficulty) {
        this.averageDifficulty = averageDifficulty;
    }

    public BigInteger getEndBlock() {
        return endBlock;
    }

    public void setEndBlock(BigInteger endBlock) {
        this.endBlock = endBlock;
    }

    public BigInteger getStartBlock() {
        return startBlock;
    }

    public void setStartBlock(BigInteger startBlock) {
        this.startBlock = startBlock;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public BigDecimal getTransactionsPerSecond() {
        return transactionsPerSecond;
    }

    public void setTransactionsPerSecond(BigDecimal transactionsPerSecond) {
        this.transactionsPerSecond = transactionsPerSecond;
    }

    public BigDecimal getAverageNrgLimit() {
        return averageNrgLimit;
    }

    public void setAverageNrgLimit(BigDecimal averageNrgLimit) {
        this.averageNrgLimit = averageNrgLimit;
    }

    public BigDecimal getAverageNrgConsumed() {
        return averageNrgConsumed;
    }

    public void setAverageNrgConsumed(BigDecimal averageNrgConsumed) {
        this.averageNrgConsumed = averageNrgConsumed;
    }

    public BigInteger getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(BigInteger totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public Integer getPeakTransactionsPerBlock() {
        return peakTransactionsPerBlock;
    }

    public void setPeakTransactionsPerBlock(Integer peakTransactionsPerBlock) {
        this.peakTransactionsPerBlock = peakTransactionsPerBlock;
    }

    public BigDecimal getPowAvgDifficulty() {
        return powAvgDifficulty;
    }

    public void setPowAvgDifficulty(BigDecimal powAvgDifficulty) {
        this.powAvgDifficulty = powAvgDifficulty;
    }

    public BigDecimal getPosAvgDifficulty() {
        return posAvgDifficulty;
    }

    public void setPosAvgDifficulty(BigDecimal posAvgDifficulty) {
        this.posAvgDifficulty = posAvgDifficulty;
    }

    public BigDecimal getPowAvgBlockTime() {
        return powAvgBlockTime;
    }

    public void setPowAvgBlockTime(BigDecimal powAvgBlockTime) {
        this.powAvgBlockTime = powAvgBlockTime;
    }

    public BigDecimal getPosAvgBlockTime() {
        return posAvgBlockTime;
    }

    public void setPosAvgBlockTime(BigDecimal posAvgBlockTime) {
        this.posAvgBlockTime = posAvgBlockTime;
    }

    public BigDecimal getPercentageOfNetworkStaking() {
        return percentageOfNetworkStaking;
    }

    public void setPercentageOfNetworkStaking(BigDecimal percentageOfNetworkStaking) {
        this.percentageOfNetworkStaking = percentageOfNetworkStaking;
    }

    public BigDecimal getTotalStake() {
        return totalStake;
    }

    public void setTotalStake(BigDecimal totalStake) {
        this.totalStake = totalStake;
    }

    public void setAverageNrgConsumedPerBlock(BigDecimal averageNrgConsumed) {
        this.averageNrgConsumed = averageNrgConsumed;
    }

    // Transient as they are Static
    public Long getBlockWindow() {
        return blockWindow;
    }

    public void setBlockWindow(Long blockWindow) {
        this.blockWindow = blockWindow;
    }

    public BigDecimal getLastBlockReward() {
        return lastBlockReward;
    }

    public void setLastBlockReward(BigDecimal blockReward) {
        lastBlockReward = blockReward;
    }

    public Long getTargetBlockTime() {
        return targetBlockTime;
    }

    public void setTargetBlockTime(Long targetBlockTime) {
        this.targetBlockTime = targetBlockTime;
    }

    public Long getCurrentBlockchainHead() {
        return currentBlockchainHead;
    }

    public void setCurrentBlockchainHead(Long currentBlockchainHead) {
        this.currentBlockchainHead = currentBlockchainHead;
    }

    public void setBlockWindow() {
        blockWindow = Math.abs(endBlock.longValueExact() - startBlock.longValueExact());
    }

    public BigDecimal getAvgPosIssuance() {
        return avgPosIssuance;
    }

    public void setAvgPosIssuance(BigDecimal avgPosIssuance) {
        this.avgPosIssuance = avgPosIssuance;
    }

    public static class CompositeKey implements Serializable {
        @Column(name = "id")
        private int id;

        @Column(name = "end_block")
        private BigInteger endBlock;


        public CompositeKey(int id, long blockNumber) {
            this.id = id;
            this.endBlock = BigInteger.valueOf(blockNumber);
        }

        public void setEndBlock(BigInteger endBlock) {
            this.endBlock = endBlock;
        }

        public BigInteger getEndBlock() {
            return endBlock;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof CompositeKey)) {
                return false;
            }
            CompositeKey that = (CompositeKey) o;
            return id == that.id && endBlock.equals(that.endBlock);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, endBlock);
        }
    }
}

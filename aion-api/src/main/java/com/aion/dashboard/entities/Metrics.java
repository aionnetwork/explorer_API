package com.aion.dashboard.entities;

import com.aion.dashboard.utility.RewardsCalculator;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.BigInteger;

@Entity
@Table(name = "Metrics")
public class Metrics {

    @Id
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

    @Transient
    private BigInteger lastBlockReward;
    @Transient
    private Long blockWindow, targetBlockTime = 10L, currentBlockchainHead;

    public Integer getId() {
        return id;
    }
    public BigDecimal getAveragedHashPower() {
        return averagedHashPower;
    }
    public BigDecimal getAveragedBlockTime() {
        return averagedBlockTime;
    }
    public BigDecimal getAverageDifficulty() {
        return averageDifficulty;
    }
    public BigInteger getEndBlock() {
        return endBlock;
    }
    public BigInteger getStartBlock() {
        return startBlock;
    }
    public Long getEndTimestamp() {
        return endTimestamp;
    }
    public Long getStartTimestamp() {
        return startTimestamp;
    }
    public BigDecimal getTransactionsPerSecond() {
        return transactionsPerSecond;
    }
    public BigDecimal getAverageNrgLimit() {
        return averageNrgLimit;
    }
    public BigDecimal getAverageNrgConsumed() {
        return averageNrgConsumed;
    }
    public BigInteger getTotalTransaction() {
        return totalTransaction;
    }
    public Integer getPeakTransactionsPerBlock() {
        return peakTransactionsPerBlock;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void setAveragedHashPower(BigDecimal averagedHashPower) {
        this.averagedHashPower = averagedHashPower;
    }
    public void setAveragedBlockTime(BigDecimal averagedBlockTime) {
        this.averagedBlockTime = averagedBlockTime;
    }
    public void setAverageDifficulty(BigDecimal averageDifficulty) {
        this.averageDifficulty = averageDifficulty;
    }
    public void setEndBlock(BigInteger endBlock) {
        this.endBlock = endBlock;
    }
    public void setStartBlock(BigInteger startBlock) {
        this.startBlock = startBlock;
    }
    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }
    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }
    public void setTransactionsPerSecond(BigDecimal transactionsPerSecond) {
        this.transactionsPerSecond = transactionsPerSecond;
    }
    public void setAverageNrgLimit(BigDecimal averageNrgLimit) {
        this.averageNrgLimit = averageNrgLimit;
    }
    public void setAverageNrgConsumedPerBlock(BigDecimal averageNrgConsumed) {
        this.averageNrgConsumed = averageNrgConsumed;
    }
    public void setPeakTransactionsPerBlock(Integer peakTransactionsPerBlock) {
        this.peakTransactionsPerBlock = peakTransactionsPerBlock;
    }
    public void setTotalTransaction(BigInteger totalTransaction) {
        this.totalTransaction = totalTransaction;
    }


    // Transient as they are Static
    public Long getBlockWindow() {
        return blockWindow;
    }
    public BigInteger getLastBlockReward() {
        return lastBlockReward;
    }
    public Long getTargetBlockTime() {
        return targetBlockTime;
    }
    public Long getCurrentBlockchainHead() {
        return currentBlockchainHead;
    }

    public void setBlockWindow() {
        blockWindow = Math.abs(endBlock.longValueExact() - startBlock.longValueExact());
    }
    public void setTargetBlockTime(Long targetBlockTime) {
        this.targetBlockTime = targetBlockTime;
    }
    public void setLastBlockReward() {
        lastBlockReward = RewardsCalculator.calculateReward(getEndBlock().longValueExact());
    }
    public void setCurrentBlockchainHead(Long currentBlockchainHead) {
        this.currentBlockchainHead = currentBlockchainHead;
    }

    public static Metrics toObject(JSONObject jsonObject) {
        Metrics metrics = new Metrics();

        metrics.setId(jsonObject.getInt("id"));
        metrics.setAveragedHashPower(BigDecimal.valueOf(jsonObject.getDouble("averagedHashPower")));
        metrics.setAveragedBlockTime(BigDecimal.valueOf(jsonObject.getDouble("averagedBlockTime")));
        metrics.setAverageDifficulty(BigDecimal.valueOf(jsonObject.getDouble("averageDifficulty")));
        metrics.setEndBlock(BigInteger.valueOf(jsonObject.getLong("endBlock")));
        metrics.setStartBlock(BigInteger.valueOf(jsonObject.getLong("startBlock")));
        metrics.setEndTimestamp(jsonObject.getLong("endTimestamp"));
        metrics.setStartTimestamp(jsonObject.getLong("startTimestamp"));
        metrics.setTransactionsPerSecond(BigDecimal.valueOf(jsonObject.getDouble("transactionsPerSecond")));
        metrics.setAverageNrgLimit(BigDecimal.valueOf(jsonObject.getDouble("averageNrgLimit")));
        metrics.setAverageNrgConsumedPerBlock(BigDecimal.valueOf(jsonObject.getDouble("averageNrgConsumed")));
        metrics.setPeakTransactionsPerBlock(jsonObject.getInt("peakTransactionsPerBlock"));
        metrics.setTotalTransaction(BigInteger.valueOf(jsonObject.getLong("totalTransaction")));


        // Transient as they are Static
        metrics.setBlockWindow();
        metrics.setLastBlockReward();
        metrics.setTargetBlockTime(jsonObject.getLong("targetBlockTime"));
        metrics.setCurrentBlockchainHead(jsonObject.getLong("currentBlockchainHead"));

        return metrics;
    }
}

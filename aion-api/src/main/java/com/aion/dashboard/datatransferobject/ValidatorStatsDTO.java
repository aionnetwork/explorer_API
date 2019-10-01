package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class ValidatorStatsDTO {
    private final long blockNumber;
    private final String minerAddress;
    private final String sealType;
    private final int blockCount;
    private final long blockTimestamp;
    private final BigDecimal percentageOfBlocksValidated;
    private final long totalBlockCount = 60480L;
    private final BigDecimal averageTransactionsPerBlock;

    public ValidatorStatsDTO(long blockNumber, String minerAddress, String sealType, int blockCount,
        long blockTimestamp, BigDecimal percentageOfBlocksValidated,
        BigDecimal averageTransactionsPerBlock) {
        this.blockNumber = blockNumber;
        this.minerAddress = minerAddress;
        this.sealType = sealType;
        this.blockCount = blockCount;
        this.blockTimestamp = blockTimestamp;
        this.percentageOfBlocksValidated = percentageOfBlocksValidated;
        this.averageTransactionsPerBlock = averageTransactionsPerBlock;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public String getSealType() {
        return sealType;
    }

    public int getBlockCount() {
        return blockCount;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public BigDecimal getPercentageOfBlocksValidated() {
        return percentageOfBlocksValidated;
    }

    public long getTotalBlockCount() {
        return totalBlockCount;
    }

    public BigDecimal getAverageTransactionsPerBlock() {
        return averageTransactionsPerBlock;
    }
}

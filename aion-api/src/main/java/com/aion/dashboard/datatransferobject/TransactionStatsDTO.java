package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class TransactionStatsDTO {
    private final long numberOfTransaction;
    private final long blockNumber;
    private final long blockTimestamp;
    private final long numberOfActiveAddresses;
    private final BigDecimal totalTransferred;

    public TransactionStatsDTO(long numberOfTransaction, long blockNumber, long blockTimestamp,
        long numberOfActiveAddresses, BigDecimal totalTransferred) {
        this.numberOfTransaction = numberOfTransaction;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.numberOfActiveAddresses = numberOfActiveAddresses;
        this.totalTransferred = totalTransferred;
    }

    public long getNumberOfTransaction() {
        return numberOfTransaction;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public long getNumberOfActiveAddresses() {
        return numberOfActiveAddresses;
    }

    public BigDecimal getTotalTransferred() {
        return totalTransferred;
    }
}

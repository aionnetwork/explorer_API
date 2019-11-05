package com.aion.dashboard.entities;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "transaction_stats")
public class TransactionStats {
    private long numberOfTransaction;
    @Id
    private long blockNumber;
    private long blockTimestamp;
    private long numberOfActiveAddresses;
    private BigDecimal totalTransferred;

    public TransactionStats() {
    }

    public TransactionStats(long numberOfTransaction, long blockNumber, long blockTimestamp,
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

    public void setNumberOfTransaction(long numberOfTransaction) {
        this.numberOfTransaction = numberOfTransaction;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public long getNumberOfActiveAddresses() {
        return numberOfActiveAddresses;
    }

    public void setNumberOfActiveAddresses(long numberOfActiveAddresses) {
        this.numberOfActiveAddresses = numberOfActiveAddresses;
    }

    public BigDecimal getTotalTransferred() {
        return totalTransferred;
    }

    public void setTotalTransferred(BigDecimal totalTransferred) {
        this.totalTransferred = totalTransferred;
    }
}

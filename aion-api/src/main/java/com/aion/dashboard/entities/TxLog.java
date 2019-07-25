package com.aion.dashboard.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tx_log")
@IdClass(TxLog.CompositeKey.class)
public class TxLog {
    @Id
    private String transactionHash;
    private long blockNumber;
    private long blockTimestamp;
    private String topics;
    private String data;
    private String contractAddr;
    private String fromAddr;
    private String toAddr;
    private String contractType;
    @Id
    private int logIndex;

    TxLog(){}

    public TxLog(String transactionHash, long blockNumber, long blockTimestamp, String topics, String data, String contractAddr, String fromAddr, String toAddr, String contractType, int logIndex) {
        this.transactionHash = transactionHash;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.topics = topics;
        this.data = data;
        this.contractAddr = contractAddr;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.contractType = contractType;
        this.logIndex = logIndex;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public TxLog setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
        return this;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public TxLog setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
        return this;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public TxLog setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public String getTopics() {
        return topics;
    }

    public TxLog setTopics(String topics) {
        this.topics = topics;
        return this;
    }

    public String getData() {
        return data;
    }

    public TxLog setData(String data) {
        this.data = data;
        return this;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public TxLog setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
        return this;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public TxLog setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
        return this;
    }

    public String getToAddr() {
        return toAddr;
    }

    public TxLog setToAddr(String toAddr) {
        this.toAddr = toAddr;
        return this;
    }

    public String getContractType() {
        return contractType;
    }

    public TxLog setContractType(String contractType) {
        this.contractType = contractType;
        return this;
    }

    public int getLogIndex() {
        return logIndex;
    }

    public TxLog setLogIndex(int logIndex) {
        this.logIndex = logIndex;
        return this;
    }

    public static class CompositeKey implements Serializable {
        @Column(name = "log_index")
        private int logIndex;
        @Column(name = "transaction_hash")
        private String transactionHash;
        public CompositeKey(){}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InternalTransfer.CompositeKey)) return false;
            InternalTransfer.CompositeKey that = (InternalTransfer.CompositeKey) o;
            return getLogIndex() == that.getTransferIndex() &&
                    Objects.equals(getTransactionHash(), that.getTransactionHash());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getLogIndex(), getTransactionHash());
        }

        public CompositeKey(int logIndex, String transactionHash) {
            this.logIndex = logIndex;
            this.transactionHash = transactionHash;
        }

        @Column(name = "log_index")
        public int getLogIndex() {
            return logIndex;
        }

        @Column(name = "log_index")
        public void setLogIndex(int logIndex) {
            this.logIndex = logIndex;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }
    }
}

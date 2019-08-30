package com.aion.dashboard.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "internal_transaction")
@IdClass(InternalTransaction.Composite.class)
public class InternalTransaction {

    private BigDecimal nrgPrice;
    private BigDecimal nrgLimit;
    private String data;
    private String kind;
    private String fromAddr;
    private String toAddr;
    private BigDecimal nonce;
    private BigDecimal value;
    private long timestamp;
    private long blockNumber;
    @Id
    private String transactionHash;
    private boolean rejected;
    @Id
    private int internalTransactionIndex;
    private String contractAddress;

    public BigDecimal getNrgPrice() {
        return nrgPrice;
    }

    public InternalTransaction setNrgPrice(BigDecimal nrgPrice) {
        this.nrgPrice = nrgPrice;
        return this;
    }

    public BigDecimal getNrgLimit() {
        return nrgLimit;
    }

    public InternalTransaction setNrgLimit(BigDecimal nrgLimit) {
        this.nrgLimit = nrgLimit;
        return this;
    }

    public String getData() {
        return data;
    }

    public InternalTransaction setData(String data) {
        this.data = data;
        return this;
    }

    public String getKind() {
        return kind;
    }

    public InternalTransaction setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public InternalTransaction setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
        return this;
    }

    public String getToAddr() {
        return toAddr;
    }

    public InternalTransaction setToAddr(String toAddr) {
        this.toAddr = toAddr;
        return this;
    }

    public BigDecimal getNonce() {
        return nonce;
    }

    public InternalTransaction setNonce(BigDecimal nonce) {
        this.nonce = nonce;
        return this;
    }

    public BigDecimal getValue() {
        return value;
    }

    public InternalTransaction setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public InternalTransaction setTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public InternalTransaction setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public InternalTransaction setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
        return this;
    }

    public boolean isRejected() {
        return rejected;
    }

    public InternalTransaction setRejected(boolean rejected) {
        this.rejected = rejected;
        return this;
    }

    public int getInternalTransactionIndex() {
        return internalTransactionIndex;
    }

    public InternalTransaction setInternalTransactionIndex(int internalTransactionIndex) {
        this.internalTransactionIndex = internalTransactionIndex;
        return this;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public InternalTransaction setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
        return this;
    }

    public static class Composite implements Serializable {
        private int internalTransactionIndex;
        private String transactionHash;

        public Composite() {
        }

        public Composite(int internalTransactionIndex, String transactionHash) {
            this.internalTransactionIndex = internalTransactionIndex;
            this.transactionHash = transactionHash;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Composite)) return false;
            Composite composite = (Composite) o;
            return internalTransactionIndex == composite.internalTransactionIndex &&
                    transactionHash.equals(composite.transactionHash);
        }

        @Override
        public int hashCode() {
            return Objects.hash(internalTransactionIndex, transactionHash);
        }

        public int getInternalTransactionIndex() {
            return internalTransactionIndex;
        }

        public Composite setInternalTransactionIndex(int internalTransactionIndex) {
            this.internalTransactionIndex = internalTransactionIndex;
            return this;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public Composite setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }
    }
}

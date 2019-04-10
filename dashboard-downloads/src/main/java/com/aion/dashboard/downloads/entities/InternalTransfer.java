package com.aion.dashboard.downloads.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "internal_transfer")
@IdClass(InternalTransfer.CompositeKey.class)
public class InternalTransfer {


    @Column(name = "from_addr")
    private String fromAddr;//contractAddr
    @Column(name = "to_addr")
    private String toAddr;//The recipient
    @Column(name = "value_transferred")
    private BigDecimal valueTransferred; // the amount transferred
    @Column(name = "block_number")
    private long blockNumber; //block number
    @Column(name = "block_timestamp")
    private long timestamp;// timestamp of transfer
    @Column(name ="transaction_hash")
    @Id
    private String transactionHash;
    @Column(name = "transfer_index")
    @Id
    private int transferIndex;


    public String getFromAddr() {
        return fromAddr;
    }

    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }



    public BigDecimal getValueTransferred() {
        return valueTransferred;
    }

    public void setValueTransferred(BigDecimal valueTransferred) {
        this.valueTransferred = valueTransferred;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalTransfer)) return false;
        InternalTransfer that = (InternalTransfer) o;
        return getBlockNumber() == that.getBlockNumber() &&
                getTimestamp() == that.getTimestamp() &&
                getTransferIndex() == that.getTransferIndex() &&
                Objects.equals(getFromAddr(), that.getFromAddr()) &&
                Objects.equals(getToAddr(), that.getToAddr()) &&
                Objects.equals(getValueTransferred(), that.getValueTransferred()) &&
                Objects.equals(getTransactionHash(), that.getTransactionHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromAddr(), getToAddr(), getValueTransferred(), getBlockNumber(), getTimestamp(), getTransactionHash(), getTransferIndex());
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public void setTransferIndex(int transferIndex) {
        this.transferIndex = transferIndex;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public int getTransferIndex() {
        return transferIndex;
    }


    public static class CompositeKey implements Serializable {
        @Column(name = "transfer_index")
        private int transferIndex;
        @Column(name = "transaction_hash")
        private String transactionHash;
        public CompositeKey(){}

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeKey)) return false;
            CompositeKey that = (CompositeKey) o;
            return getTransferIndex() == that.getTransferIndex() &&
                    Objects.equals(getTransactionHash(), that.getTransactionHash());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTransferIndex(), getTransactionHash());
        }

        public CompositeKey(int transferIndex, String transactionHash) {
            this.transferIndex = transferIndex;
            this.transactionHash = transactionHash;
        }

        @Column(name = "transfer_index")
        public int getTransferIndex() {
            return transferIndex;
        }

        @Column(name = "transfer_index")
        public void setTransferIndex(int transferIndex) {
            this.transferIndex = transferIndex;
        }

        public String getTransactionHash() {
            return transactionHash;
        }

        public void setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
        }
    }


}


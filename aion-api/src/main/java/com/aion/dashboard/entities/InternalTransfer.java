package com.aion.dashboard.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "internal_transfer")
@IdClass(InternalTransfer.CompositeKey.class)
public class InternalTransfer {

    private String fromAddr;//contractAddr
    private String toAddr;//The recipient
    private BigDecimal valueTransferred; // the amount transferred
    private long blockNumber; //block number
    private long blockTimestamp;// blockTimestamp of transfer
    @Id
    private String transactionHash;
    @Id
    private int transferIndex;


    public InternalTransfer() {
        //Constructor
    }


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

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalTransfer)) return false;
        InternalTransfer that = (InternalTransfer) o;
        return getBlockNumber() == that.getBlockNumber() &&
                getBlockTimestamp() == that.getBlockTimestamp() &&
                getTransferIndex() == that.getTransferIndex() &&
                Objects.equals(getFromAddr(), that.getFromAddr()) &&
                Objects.equals(getToAddr(), that.getToAddr()) &&
                Objects.equals(getValueTransferred(), that.getValueTransferred()) &&
                Objects.equals(getTransactionHash(), that.getTransactionHash());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFromAddr(), getToAddr(), getValueTransferred(), getBlockNumber(), getBlockTimestamp(), getTransactionHash(), getTransferIndex());
    }

    public void setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
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


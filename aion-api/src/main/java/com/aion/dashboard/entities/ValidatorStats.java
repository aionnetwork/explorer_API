package com.aion.dashboard.entities;

import com.aion.dashboard.entities.ValidatorStats.CompositeKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "validator_stats")
@IdClass(CompositeKey.class)
public class ValidatorStats {
    @Id
    private long blockNumber;
    @Id
    private String minerAddress;
    @Id
    private String sealType;
    private int blockCount;
    private long blockTimestamp;
    private BigDecimal percentageOfBlocksValidated;
    private BigDecimal averageTransactionsPerBlock;


    public ValidatorStats(long blockNumber, String minerAddress, String sealType, int blockCount,
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

    public ValidatorStats() {
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }

    public void setSealType(String sealType) {
        this.sealType = sealType;
    }

    public void setBlockCount(int blockCount) {
        this.blockCount = blockCount;
    }

    public void setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public void setPercentageOfBlocksValidated(BigDecimal percentageOfBlocksValidated) {
        this.percentageOfBlocksValidated = percentageOfBlocksValidated;
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

    public BigDecimal getAverageTransactionsPerBlock() {
        if (averageTransactionsPerBlock.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        } else {
            return averageTransactionsPerBlock;
        }
    }

    public void setAverageTransactionsPerBlock(BigDecimal averageTransactionsPerBlock) {
        this.averageTransactionsPerBlock = averageTransactionsPerBlock;
    }

    public static class CompositeKey implements Serializable {
        private long blockNumber;
        private String minerAddress;
        private String sealType;

        public CompositeKey() {
        }

        public CompositeKey(long blockNumber, String minerAddress, String sealType) {
            this.blockNumber = blockNumber;
            this.minerAddress = minerAddress;
            this.sealType = sealType;
        }

        public long getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(long blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getMinerAddress() {
            return minerAddress;
        }

        public void setMinerAddress(String minerAddress) {
            this.minerAddress = minerAddress;
        }

        public String getSealType() {
            return sealType;
        }

        public void setSealType(String sealType) {
            this.sealType = sealType;
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
            return blockNumber == that.blockNumber &&
                Objects.equals(minerAddress, that.minerAddress) &&
                Objects.equals(sealType, that.sealType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(blockNumber, minerAddress, sealType);
        }
    }
}

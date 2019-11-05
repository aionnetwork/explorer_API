package com.aion.dashboard.entities;

import com.aion.dashboard.entities.AccountStats.Composite;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "account_stats")
@IdClass(Composite.class)
public class AccountStats {
    @Id
    private long blockNumber;
    @Id
    private String address;
    private BigDecimal aionIn;
    private BigDecimal aionOut;
    private long blockTimestamp;

    public AccountStats(long blockNumber, String address, BigDecimal aionIn,
        BigDecimal aionOut, long blockTimestamp) {
        this.blockNumber = blockNumber;
        this.address = address;
        this.aionIn = aionIn;
        this.aionOut = aionOut;
        this.blockTimestamp = blockTimestamp;
    }

    public AccountStats() {
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getAionIn() {
        return aionIn;
    }

    public void setAionIn(BigDecimal aionIn) {
        this.aionIn = aionIn;
    }

    public BigDecimal getAionOut() {
        return aionOut;
    }

    public void setAionOut(BigDecimal aionOut) {
        this.aionOut = aionOut;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public static class Composite implements Serializable {
        private long blockNumber;
        private String address;

        public Composite(long blockNumber, String address) {
            this.blockNumber = blockNumber;
            this.address = address;
        }

        public Composite() {
        }

        public long getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(long blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Composite)) {
                return false;
            }
            Composite composite = (Composite) o;
            return blockNumber == composite.blockNumber &&
                address == composite.address;
        }

        @Override
        public int hashCode() {
            return Objects.hash(blockNumber, address);
        }
    }
}

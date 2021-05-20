package com.aion.dashboard.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="circulating_supply")
public class CirculatingSupply {

    @Id
    private BigDecimal blockNumber;
    private Timestamp timestamp;
    private BigDecimal supply;
    private String totalBlockReward;

    public CirculatingSupply() {
    }

    public CirculatingSupply(Timestamp timestamp, BigDecimal supply) {
        this.timestamp = timestamp;
        this.supply = supply;
    }

    public CirculatingSupply(Timestamp timestamp, BigDecimal supply, String totalBlockReward, BigDecimal blockNumber) {
        this.timestamp = timestamp;
        this.supply = supply;
        this.totalBlockReward = totalBlockReward;
        this.blockNumber = blockNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public BigDecimal getSupply() {
        return supply;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setSupply(BigDecimal supply) {
        this.supply = supply;
    }

    public String getTotalBlockReward() {
        return totalBlockReward;
    }

    public void setTotalBlockReward(String totalBlockReward) {
        this.totalBlockReward = totalBlockReward;
    }

    public BigDecimal getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigDecimal blockNumber) {
        this.blockNumber = blockNumber;
    }
}

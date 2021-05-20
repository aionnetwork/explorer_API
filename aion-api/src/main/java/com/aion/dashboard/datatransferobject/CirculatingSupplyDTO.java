package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;
import java.sql.Timestamp;


public class CirculatingSupplyDTO {

    private Timestamp timestamp;
    private String supply;
    private BigDecimal blockNumber;

    public CirculatingSupplyDTO() {
    }

    public CirculatingSupplyDTO(Timestamp timestamp, String supply) {
        this.timestamp = timestamp;
        this.supply = supply;
    }

    public CirculatingSupplyDTO(Timestamp timestamp, String supply,  BigDecimal blockNumber) {
        this.timestamp = timestamp;
        this.supply = supply;
         this.blockNumber = blockNumber;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getSupply() {
        return supply;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public BigDecimal getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigDecimal blockNumber) {
        this.blockNumber = blockNumber;
    }
}

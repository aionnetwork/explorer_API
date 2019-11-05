package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class AccountStatsDTO {
    private final long blockNumber;
    private final String address;
    private final BigDecimal aionIn;
    private final BigDecimal aionOut;
    private final long blockTimestamp;

    public AccountStatsDTO(long blockNumber, String address, BigDecimal aionIn,
        BigDecimal aionOut, long blockTimestamp) {
        this.blockNumber = blockNumber;
        this.address = address;
        this.aionIn = aionIn;
        this.aionOut = aionOut;
        this.blockTimestamp = blockTimestamp;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getAionIn() {
        return aionIn;
    }

    public BigDecimal getAionOut() {
        return aionOut;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }
}

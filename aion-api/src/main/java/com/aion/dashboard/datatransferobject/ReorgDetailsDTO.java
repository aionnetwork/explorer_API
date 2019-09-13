package com.aion.dashboard.datatransferobject;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class ReorgDetailsDTO {
    private long blockNumber;
    private ZonedDateTime serverTimestamp;
    private int blockDepth;
    private List<String> affectedAddresses;
    private long numberOfAffectedTransactions;

    public ReorgDetailsDTO(long blockNumber, Timestamp serverTimestamp, int blockDepth, String affectedAddresses, long numberOfAffectedTransactions) {
        this.blockNumber = blockNumber;
        this.serverTimestamp = serverTimestamp.toInstant().atZone(ZoneId.of("UTC"));
        this.blockDepth = blockDepth;
        this.affectedAddresses = Arrays.asList(affectedAddresses.replaceAll("(\\[|])", "").split(","));
        this.numberOfAffectedTransactions = numberOfAffectedTransactions;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public String getServerTimestamp() {
        return serverTimestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public int getBlockDepth() {
        return blockDepth;
    }

    public List<String> getAffectedAddresses() {
        return affectedAddresses;
    }

    public long getNumberOfAffectedTransactions() {
        return numberOfAffectedTransactions;
    }


}

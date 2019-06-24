package com.aion.dashboard.datatransferobject;

public class HealthDTO {
    private final long dbBlockHead;
    private final long blockchainHead;
    private final long timestamp;
    private final String status;

    public HealthDTO(long dbBlockHead, long blockchainHead, long timestamp, String status) {
        this.dbBlockHead = dbBlockHead;
        this.blockchainHead = blockchainHead;
        this.timestamp = timestamp;
        this.status = status;
    }

    public long getDbBlockHead() {
        return dbBlockHead;
    }

    public long getBlockchainHead() {
        return blockchainHead;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }
}

package com.aion.dashboard.entities;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reorg_details")
public class ReorgDetails {
    @Id
    private long id;
    private long blockNumber;
    private Timestamp serverTimestamp;
    private int blockDepth;
    private String affectedAddresses;
    private long numberOfAffectedTransactions;

    public ReorgDetails() {
    }

    public long getId() {
        return id;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public Timestamp getServerTimestamp() {
        return serverTimestamp;
    }

    public int getBlockDepth() {
        return blockDepth;
    }

    public String getAffectedAddresses() {
        return affectedAddresses;
    }

    public long getNumberOfAffectedTransactions() {
        return numberOfAffectedTransactions;
    }

    public ReorgDetails setId(long id) {
        this.id = id;
        return this;
    }

    public ReorgDetails setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public ReorgDetails setServerTimestamp(Timestamp serverTimestamp) {
        this.serverTimestamp = serverTimestamp;
        return this;
    }

    public ReorgDetails setBlockDepth(int blockDepth) {
        this.blockDepth = blockDepth;
        return this;
    }

    public ReorgDetails setAffectedAddresses(String affectedAddress) {
        this.affectedAddresses = affectedAddress;
        return this;
    }

    public ReorgDetails setNumberOfAffectedTransactions(long numberOfAffectedTransactions) {
        this.numberOfAffectedTransactions = numberOfAffectedTransactions;
        return this;
    }
}

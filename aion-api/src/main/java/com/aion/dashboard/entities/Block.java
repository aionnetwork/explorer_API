package com.aion.dashboard.entities;


import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name="Block")
public class Block {

    @Id
    private Long blockNumber;
    private Long nrgConsumed;
    private Long nrgLimit;
    @Column(name = "block_size")
    private Long size;
    private Long blockTimestamp;
    private Long numTransactions;
    private Long blockTime;
    private Long nrgReward;
    private Long difficulty;
    private Long totalDifficulty;
    private Integer year;
    private Integer month;
    private Integer day;
    private String blockHash;
    private String minerAddress;
    private String parentHash;
    private String receiptTxRoot;
    private String stateRoot;
    private String txTrieRoot;
    private String extraData;
    private String nonce;
    private String bloom;
    private String solution;
    private String transactionHash;
    private String transactionHashes;

    @Transient
    private BigInteger blockReward;

    public Long getBlockNumber() {
        return blockNumber;
    }
    public Long getNrgConsumed() {
        return nrgConsumed;
    }
    public Long getNrgLimit() {
        return nrgLimit;
    }
    public Long getSize() {
        return size;
    }
    public Long getBlockTimestamp() {
        return blockTimestamp;
    }
    public Long getNumTransactions() {
        return numTransactions;
    }
    public Long getBlockTime() {
        return blockTime;
    }
    public String getTransactionHash() {
        return transactionHash;
    }
    public Long getDifficulty() {
        return difficulty;
    }
    public Long getTotalDifficulty() {
        return totalDifficulty;
    }
    public Long getNrgReward() {
        return nrgReward;
    }
    public Integer getYear() {
        return year;
    }
    public Integer getMonth() {
        return month;
    }
    public Integer getDay() {
        return day;
    }
    public String getBlockHash() {
        return blockHash;
    }
    public String getMinerAddress() {
        return minerAddress;
    }
    public String getParentHash() {
        return parentHash;
    }
    public String getTransactionHashes() {
        return transactionHashes;
    }
    public String getReceiptTxRoot() {
        return receiptTxRoot;
    }
    public String getStateRoot() {
        return stateRoot;
    }
    public String getTxTrieRoot() {
        return txTrieRoot;
    }
    public String getExtraData() {
        return extraData;
    }
    public String getNonce() {
        return nonce;
    }
    public String getBloom() {
        return bloom;
    }
    public String getSolution() {
        return solution;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }
    public void setNrgConsumed(Long nrgConsumed) {
        this.nrgConsumed = nrgConsumed;
    }
    public void setNrgLimit(Long nrgLimit) {
        this.nrgLimit = nrgLimit;
    }
    public void setSize(Long size) {
        this.size = size;
    }
    public void setBlockTimestamp(Long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }
    public void setNumTransactions(Long numTransactions) {
        this.numTransactions = numTransactions;
    }
    public void setBlockTime(Long blockTime) {
        this.blockTime = blockTime;
    }
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
    public void setDifficulty(Long difficulty) {
        this.difficulty = difficulty;
    }
    public void setTotalDifficulty(Long totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }
    public void setNrgReward(Long nrgReward) {
        this.nrgReward = nrgReward;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }
    public void setDay(Integer day) {
        this.day = day;
    }
    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }
    public void setTransactionHashes(String transactionHashes) {
        this.transactionHashes = transactionHashes;
    }
    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }
    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }
    public void setReceiptTxRoot(String receiptTxRoot) {
        this.receiptTxRoot = receiptTxRoot;
    }
    public void setStateRoot(String stateRoot) {
        this.stateRoot = stateRoot;
    }
    public void setTxTrieRoot(String txTrieRoot) {
        this.txTrieRoot = txTrieRoot;
    }
    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public void setBloom(String bloom) {
        this.bloom = bloom;
    }
    public void setSolution(String solution) {
        this.solution = solution;
    }



    public BigInteger getBlockReward() {
        return blockReward;
    }
    public void setBlockReward(BigInteger blockReward) {
        this.blockReward = blockReward;
    }
}
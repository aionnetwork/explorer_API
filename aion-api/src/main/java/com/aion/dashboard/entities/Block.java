package com.aion.dashboard.entities;


import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private BigDecimal nrgReward;
    private BigDecimal difficulty;
    private BigDecimal totalDifficulty;
    private BigDecimal blockReward;
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
    private String sealType;
    private String signature;
    private String seed;
    private String publicKey;
    private String coinbaseAddress;


    public Block(){}

    public Block(Long blockNumber, Long nrgConsumed, Long nrgLimit, Long size, Long blockTimestamp,
        Long numTransactions, Long blockTime, BigDecimal nrgReward, BigDecimal difficulty,
        BigDecimal totalDifficulty, Integer year, Integer month, Integer day, String blockHash,
        String minerAddress, String parentHash, String receiptTxRoot, String stateRoot,
        String txTrieRoot, String extraData, String nonce, String bloom, String solution,
        String transactionHash, String transactionHashes, BigDecimal blockReward,
        String coinbaseAddress) {
        this.blockNumber = blockNumber;
        this.nrgConsumed = nrgConsumed;
        this.nrgLimit = nrgLimit;
        this.size = size;
        this.blockTimestamp = blockTimestamp;
        this.numTransactions = numTransactions;
        this.blockTime = blockTime;
        this.nrgReward = nrgReward;
        this.difficulty = difficulty;
        this.totalDifficulty = totalDifficulty;
        this.year = year;
        this.month = month;
        this.day = day;
        this.blockHash = blockHash;
        this.minerAddress = minerAddress;
        this.parentHash = parentHash;
        this.receiptTxRoot = receiptTxRoot;
        this.stateRoot = stateRoot;
        this.txTrieRoot = txTrieRoot;
        this.extraData = extraData;
        this.nonce = nonce;
        this.bloom = bloom;
        this.solution = solution;
        this.transactionHash = transactionHash;
        this.transactionHashes = transactionHashes;
        this.blockReward = blockReward;
        this.coinbaseAddress = coinbaseAddress;
    }

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
    public BigDecimal getDifficulty() {
        return difficulty;
    }
    public BigDecimal getTotalDifficulty() {
        return totalDifficulty;
    }
    public BigDecimal getNrgReward() {
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
    public String getSealType() {
        return sealType;
    }
    public String getSignature() {
        return signature;
    }
    public String getSeed() {
        return seed;
    }
    public String getPublicKey() {
        return publicKey;
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
    public void setDifficulty(BigDecimal difficulty) {
        this.difficulty = difficulty;
    }
    public void setTotalDifficulty(BigDecimal totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }
    public void setNrgReward(BigDecimal nrgReward) {
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
    public Block setSealType(String sealType) {
        this.sealType = sealType;
        return this;
    }
    public Block setSignature(String signature) {
        this.signature = signature;
        return this;
    }
    public Block setSeed(String seed) {
        this.seed = seed;
        return this;
    }
    public Block setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public BigDecimal getBlockReward() {
        return blockReward;
    }
    public void setBlockReward(BigDecimal blockReward) {
        this.blockReward = blockReward;
    }

    public String getCoinbaseAddress() {
        return coinbaseAddress;
    }

    public void setCoinbaseAddress(String coinbaseAddress) {
        this.coinbaseAddress = coinbaseAddress;
    }
}
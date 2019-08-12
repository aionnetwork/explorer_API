package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BlockDTO {




    private Long blockNumber;
    private Long nrgConsumed;
    private Long nrgLimit;
    private Long size;
    private Long blockTimestamp;
    private Long numTransactions;
    private Long blockTime;
    private BigDecimal nrgReward;
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


    private BigDecimal blockReward;

    public BlockDTO(){}

    public BlockDTO(Long blockNumber, Long nrgConsumed, Long nrgLimit, Long size, Long blockTimestamp, Long numTransactions, Long blockTime, BigDecimal nrgReward, Long difficulty, Long totalDifficulty, Integer year, Integer month, Integer day, String blockHash, String minerAddress, String parentHash, String receiptTxRoot, String stateRoot, String txTrieRoot, String extraData, String nonce, String bloom, String solution, String transactionHash, String transactionHashes, BigDecimal blockReward) {
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
    public Long getDifficulty() {
        return difficulty;
    }
    public Long getTotalDifficulty() {
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



    public BigDecimal getBlockReward() {
        return blockReward;
    }
    public void setBlockReward(BigDecimal blockReward) {
        this.blockReward = blockReward;
    }


    public static BlockDTOBuilder newBuilder()
    {
        return new BlockDTOBuilder();
    }


    public static class BlockDTOBuilder{
        private Long blockNumber;
        private Long nrgConsumed;
        private Long nrgLimit;
        private Long size;
        private Long blockTimestamp;
        private Long numTransactions;
        private Long blockTime;
        private BigDecimal nrgReward;
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
        private BigDecimal blockReward;

        public BlockDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public BlockDTOBuilder setNrgConsumed(Long nrgConsumed) {
            this.nrgConsumed = nrgConsumed;
            return this;
        }

        public BlockDTOBuilder setNrgLimit(Long nrgLimit) {
            this.nrgLimit = nrgLimit;
            return this;
        }

        public BlockDTOBuilder setSize(Long size) {
            this.size = size;
            return this;
        }

        public BlockDTOBuilder setBlockTimestamp(Long blockTimestamp) {
            this.blockTimestamp = blockTimestamp;
            return this;
        }

        public BlockDTOBuilder setNumTransactions(Long numTransactions) {
            this.numTransactions = numTransactions;
            return this;
        }

        public BlockDTOBuilder setBlockTime(Long blockTime) {
            this.blockTime = blockTime;
            return this;
        }

        public BlockDTOBuilder setNrgReward(BigDecimal nrgReward) {
            this.nrgReward = nrgReward;
            return this;
        }

        public BlockDTOBuilder setDifficulty(Long difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public BlockDTOBuilder setTotalDifficulty(Long totalDifficulty) {
            this.totalDifficulty = totalDifficulty;
            return this;
        }

        public BlockDTOBuilder setYear(Integer year) {
            this.year = year;
            return this;
        }

        public BlockDTOBuilder setMonth(Integer month) {
            this.month = month;
            return this;
        }

        public BlockDTOBuilder setDay(Integer day) {
            this.day = day;
            return this;
        }

        public BlockDTOBuilder setBlockHash(String blockHash) {
            this.blockHash = blockHash;
            return this;
        }

        public BlockDTOBuilder setMinerAddress(String minerAddress) {
            this.minerAddress = minerAddress;
            return this;
        }

        public BlockDTOBuilder setParentHash(String parentHash) {
            this.parentHash = parentHash;
            return this;
        }

        public BlockDTOBuilder setReceiptTxRoot(String receiptTxRoot) {
            this.receiptTxRoot = receiptTxRoot;
            return this;
        }

        public BlockDTOBuilder setStateRoot(String stateRoot) {
            this.stateRoot = stateRoot;
            return this;
        }

        public BlockDTOBuilder setTxTrieRoot(String txTrieRoot) {
            this.txTrieRoot = txTrieRoot;
            return this;
        }

        public BlockDTOBuilder setExtraData(String extraData) {
            this.extraData = extraData;
            return this;
        }

        public BlockDTOBuilder setNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public BlockDTOBuilder setBloom(String bloom) {
            this.bloom = bloom;
            return this;
        }

        public BlockDTOBuilder setSolution(String solution) {
            this.solution = solution;
            return this;
        }

        public BlockDTOBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public BlockDTOBuilder setTransactionHashes(String transactionHashes) {
            this.transactionHashes = transactionHashes;
            return this;
        }

        public BlockDTOBuilder setBlockReward(BigDecimal blockReward) {
            this.blockReward = blockReward;
            return this;
        }

        public BlockDTO createBlockDTO() {
            return new BlockDTO(blockNumber, nrgConsumed, nrgLimit, size, blockTimestamp, numTransactions, blockTime, nrgReward, difficulty, totalDifficulty, year, month, day, blockHash, minerAddress, parentHash, receiptTxRoot, stateRoot, txTrieRoot, extraData, nonce, bloom, solution, transactionHash, transactionHashes, blockReward);
        }    }
}

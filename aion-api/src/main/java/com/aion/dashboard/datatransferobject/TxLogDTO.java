package com.aion.dashboard.datatransferobject;

import java.util.List;

public class TxLogDTO {
    private String transactionHash;
    private long blockNumber;
    private long blockTimestamp;
    private List<String> topics;
    private String data;
    private String contractAddr;
    private String fromAddr;
    private String toAddr;
    private String contractType;
    private int logIndex;

    public TxLogDTO(String transactionHash, long blockNumber, long blockTimestamp, List<String> topics, String data, String contractAddr, String fromAddr, String toAddr, String contractType, int logIndex) {
        this.transactionHash = transactionHash;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.topics = topics;
        this.data = data;
        this.contractAddr = contractAddr;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.contractType = contractType;
        this.logIndex = logIndex;
    }

    public TxLogDTO() {
    }


    public TxLogDTO setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
        return this;
    }

    public TxLogDTO setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }

    public TxLogDTO setBlockTimestamp(long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
        return this;
    }

    public TxLogDTO setTopics(List<String> topics) {
        this.topics = topics;
        return this;
    }

    public TxLogDTO setData(String data) {
        this.data = data;
        return this;
    }

    public TxLogDTO setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
        return this;
    }

    public TxLogDTO setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
        return this;
    }

    public TxLogDTO setToAddr(String toAddr) {
        this.toAddr = toAddr;
        return this;
    }

    public TxLogDTO setContractType(String contractType) {
        this.contractType = contractType;
        return this;
    }

    public TxLogDTO setLogIndex(int logIndex) {
        this.logIndex = logIndex;
        return this;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public List<String> getTopics() {
        return topics;
    }

    public String getData() {
        return data;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public String getContractType() {
        return contractType;
    }

    public int getLogIndex() {
        return logIndex;
    }


    public static class TxLogDTOBuilder{

        private String transactionHash;
        private long blockNumber;
        private long blockTimestamp;
        private List<String> topics;
        private String data;
        private String contractAddr;
        private String fromAddr;
        private String toAddr;
        private String contractType;
        private int logIndex;

        public TxLogDTOBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public TxLogDTOBuilder setBlockNumber(long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public TxLogDTOBuilder setBlockTimestamp(long blockTimestamp) {
            this.blockTimestamp = blockTimestamp;
            return this;
        }

        public TxLogDTOBuilder setTopics(List<String> topics) {
            this.topics = topics;
            return this;
        }

        public TxLogDTOBuilder setData(String data) {
            this.data = data;
            return this;
        }

        public TxLogDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public TxLogDTOBuilder setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
            return this;
        }

        public TxLogDTOBuilder setToAddr(String toAddr) {
            this.toAddr = toAddr;
            return this;
        }

        public TxLogDTOBuilder setContractType(String contractType) {
            this.contractType = contractType;
            return this;
        }

        public TxLogDTOBuilder setLogIndex(int logIndex) {
            this.logIndex = logIndex;
            return this;
        }


        public TxLogDTO build(){
            return new TxLogDTO(transactionHash,
                    blockNumber,
                    blockTimestamp,
                    topics,
                    data,
                    contractAddr,
                    fromAddr,
                    toAddr,
                    contractType,
                    logIndex);
        }
    }
}

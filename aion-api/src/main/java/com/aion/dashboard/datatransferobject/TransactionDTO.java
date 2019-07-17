package com.aion.dashboard.datatransferobject;


import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;


public class TransactionDTO  {



    private TransactionDTO(String transactionHash, String blockHash, Long blockNumber, Long blockTimestamp, Long transactionIndex, Long nrgConsumed, Long nrgPrice, Long transactionTimestamp, Integer year, Integer month, Integer day, String fromAddr, String toAddr, String transactionLog, String data, String nonce, String txError, String contractAddr, BigDecimal value, String type) {
        this.transactionHash = transactionHash;
        this.blockHash = blockHash;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
        this.transactionIndex = transactionIndex;
        this.nrgConsumed = nrgConsumed;
        this.nrgPrice = nrgPrice;
        this.transactionTimestamp = transactionTimestamp;
        this.year = year;
        this.month = month;
        this.day = day;
        this.fromAddr = fromAddr;
        this.toAddr = toAddr;
        this.transactionLog = transactionLog;
        this.data = data;
        this.nonce = nonce;
        this.txError = txError;
        this.contractAddr = contractAddr;
        this.value = value;
        this.type = type;
    }

    private String transactionHash;
    private String blockHash;

    private Long blockNumber;

    private Long blockTimestamp;
    private Long transactionIndex;
    private Long nrgConsumed;
    private Long nrgPrice;
    private Long transactionTimestamp;
    private Integer year;
    private Integer month;
    private Integer day;

    private String fromAddr;
    private String toAddr;
    private String transactionLog;
    private String data;
    private String nonce;
    private String txError;
    private String contractAddr;
    private BigDecimal value;
    private String type;



    public Long getBlockNumber() {
        return blockNumber;
    }
    public Long getBlockTimestamp() {
        return blockTimestamp;
    }
    public Long getTransactionIndex() {
        return transactionIndex;
    }
    public Long getNrgConsumed() {
        return nrgConsumed;
    }
    public Long getNrgPrice() {
        return nrgPrice;
    }
    public Long getTransactionTimestamp() {
        return transactionTimestamp;
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
    public String getTransactionHash() {
        return transactionHash;
    }
    public String getContractAddr() {
        return contractAddr;
    }
    public String getTxError() {
        return txError;
    }
    public String getBlockHash() {
        return blockHash;
    }
    public String getFromAddr() {
        return fromAddr;
    }
    public String getToAddr() {
        return toAddr;
    }
    public String getTransactionLog() {
        return transactionLog;
    }
    public String getNonce() {
        return nonce;
    }
    public String getData() {
        return data;
    }
    public BigDecimal getValue() {
        return value;
    }
    public String getType() {
        return type;
    }


    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }
    public void setBlockTimestamp(Long blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }
    public void setTransactionIndex(Long transactionIndex) {
        this.transactionIndex = transactionIndex;
    }
    public void setNrgConsumed(Long nrgConsumed) {
        this.nrgConsumed = nrgConsumed;
    }
    public void setNrgPrice(Long nrgPrice) {
        this.nrgPrice = nrgPrice;
    }
    public void setTransactionTimestamp(Long transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
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
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }
    public void setTransactionLog(String transactionLog) {
        this.transactionLog = transactionLog;
    }
    public void setData(String data) {
        this.data = data;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public void setTxError(String txError) {
        this.txError = txError;
    }
    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public void setType(String type){
        this.type = type;
    }


    public static TransactionDTOBuilder newBuilder()
    {
        return new TransactionDTOBuilder();
    }


    public static class TransactionDTOBuilder {
        private String transactionHash;
        private String blockHash;
        private Long blockNumber;
        private Long blockTimestamp;
        private Long transactionIndex;
        private Long nrgConsumed;
        private Long nrgPrice;
        private Long transactionTimestamp;
        private Integer year;
        private Integer month;
        private Integer day;
        private String fromAddr;
        private String toAddr;
        private String transactionLog;
        private String data;
        private String nonce;
        private String txError;
        private String contractAddr;
        private BigDecimal value;
        private String type;


        public TransactionDTOBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public TransactionDTOBuilder setBlockHash(String blockHash) {
            this.blockHash = blockHash;
            return this;
        }

        public TransactionDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public TransactionDTOBuilder setBlockTimestamp(Long blockTimestamp) {
            this.blockTimestamp = blockTimestamp;
            return this;
        }

        public TransactionDTOBuilder setTransactionIndex(Long transactionIndex) {
            this.transactionIndex = transactionIndex;
            return this;
        }

        public TransactionDTOBuilder setNrgConsumed(Long nrgConsumed) {
            this.nrgConsumed = nrgConsumed;
            return this;
        }

        public TransactionDTOBuilder setNrgPrice(Long nrgPrice) {
            this.nrgPrice = nrgPrice;
            return this;
        }

        public TransactionDTOBuilder setTransactionTimestamp(Long transactionTimestamp) {
            this.transactionTimestamp = transactionTimestamp;
            return this;
        }

        public TransactionDTOBuilder setYear(Integer year) {
            this.year = year;
            return this;
        }

        public TransactionDTOBuilder setMonth(Integer month) {
            this.month = month;
            return this;
        }

        public TransactionDTOBuilder setDay(Integer day) {
            this.day = day;
            return this;
        }

        public TransactionDTOBuilder setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
            return this;
        }

        public TransactionDTOBuilder setToAddr(String toAddr) {
            this.toAddr = toAddr;
            return this;
        }

        public TransactionDTOBuilder setTransactionLog(String transactionLog) {
            this.transactionLog = transactionLog;
            return this;
        }

        public TransactionDTOBuilder setData(String data) {
            this.data = data;
            return this;
        }

        public TransactionDTOBuilder setNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public TransactionDTOBuilder setTxError(String txError) {
            this.txError = txError;
            return this;
        }

        public TransactionDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public TransactionDTOBuilder setValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        public TransactionDTOBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public TransactionDTO createTransactionDTO() {
            return new TransactionDTO(transactionHash, blockHash, blockNumber, blockTimestamp, transactionIndex, nrgConsumed, nrgPrice, transactionTimestamp, year, month, day, fromAddr, toAddr, transactionLog, data, nonce, txError, contractAddr, value, type);
        }
    }

}
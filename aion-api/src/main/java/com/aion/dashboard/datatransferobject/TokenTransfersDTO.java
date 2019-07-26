package com.aion.dashboard.datatransferobject;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.math.BigDecimal;

public class TokenTransfersDTO {

    private Long blockNumber;
    private Long transferTimestamp;
    private String transactionHash;
    private String toAddr;
    private String fromAddr;
    private String rawValue;
    private String contractAddr;
    private String operatorAddr;
    private BigDecimal tknValue;
    private Integer tokenDecimal;
    private BigDecimal granularity;

    public TokenTransfersDTO(Long blockNumber, Long transferTimestamp, String transactionHash, String toAddr, String fromAddr, String rawValue, String contractAddr, String operatorAddr, BigDecimal tknValue, Integer tokenDecimal, BigDecimal granularity) {
        this.blockNumber = blockNumber;
        this.transferTimestamp = transferTimestamp;
        this.transactionHash = transactionHash;
        this.toAddr = toAddr;
        this.fromAddr = fromAddr;
        this.rawValue = rawValue;
        this.contractAddr = contractAddr;
        this.operatorAddr = operatorAddr;
        this.tknValue = tknValue;
        this.tokenDecimal = tokenDecimal;
        this.granularity = granularity;
    }



    public Long getBlockNumber() {
        return blockNumber;
    }

    public Long getTransferTimestamp() {
        return transferTimestamp;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getToAddr() {
        return toAddr;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public String getRawValue() {
        return rawValue;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public String getOperatorAddr() {
        return operatorAddr;
    }

    @JsonGetter("scaledValue")
    public BigDecimal getTknValue() {
        return tknValue;
    }

    public Integer getTokenDecimal() {
        return tokenDecimal;
    }

    public BigDecimal getGranularity() {
        return granularity;
    }



    public static class TokenTransfersDTOBuilder{
        private Long blockNumber;
        private Long transferTimestamp;
        private String transactionHash;
        private String toAddr;
        private String fromAddr;
        private String rawValue;
        private String contractAddr;
        private String operatorAddr;
        private BigDecimal tknValue;
        private Integer tokenDecimal;
        private BigDecimal granularity;

        public TokenTransfersDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public TokenTransfersDTOBuilder setTransferTimestamp(Long transferTimestamp) {
            this.transferTimestamp = transferTimestamp;
            return this;
        }

        public TokenTransfersDTOBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public TokenTransfersDTOBuilder setToAddr(String toAddr) {
            this.toAddr = toAddr;
            return this;
        }

        public TokenTransfersDTOBuilder setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
            return this;
        }

        public TokenTransfersDTOBuilder setRawValue(String rawValue) {
            this.rawValue = rawValue;
            return this;
        }

        public TokenTransfersDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public TokenTransfersDTOBuilder setOperatorAddr(String operatorAddr) {
            this.operatorAddr = operatorAddr;
            return this;
        }

        public TokenTransfersDTOBuilder setTknValue(BigDecimal tknValue) {
            this.tknValue = tknValue;
            return this;
        }

        public TokenTransfersDTOBuilder setTokenDecimal(Integer tokenDecimal) {
            this.tokenDecimal = tokenDecimal;
            return this;
        }

        public TokenTransfersDTOBuilder setGranularity(BigDecimal granularity) {
            this.granularity = granularity;
            return this;
        }


        public TokenTransfersDTO createTokenTransfersDTO(){
            return new TokenTransfersDTO(blockNumber, transferTimestamp, transactionHash, toAddr, fromAddr, rawValue, contractAddr, operatorAddr, tknValue, tokenDecimal, granularity);
        }
    }
}

package com.aion.dashboard.datatransferobject;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.math.BigDecimal;

public class TokenHoldersDTO {

    private String holderAddr;
    private String contractAddr;
    private String rawBalance;
    private BigDecimal tknBalance;
    private Integer tokenDecimal;
    private Long blockNumber;
    private BigDecimal granularity;

    public TokenHoldersDTO(String holderAddr, String contractAddr, String rawBalance, BigDecimal tknBalance, Integer tokenDecimal, Long blockNumber, BigDecimal granularity) {
        this.holderAddr = holderAddr;
        this.contractAddr = contractAddr;
        this.rawBalance = rawBalance;
        this.tknBalance = tknBalance;
        this.tokenDecimal = tokenDecimal;
        this.blockNumber = blockNumber;
        this.granularity = granularity;
    }


    public String getHolderAddr() {
        return holderAddr;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public String getRawBalance() {
        return rawBalance;
    }

    @JsonGetter("scaledBalance")
    public BigDecimal getTknBalance() {
        return tknBalance;
    }

    public Integer getTokenDecimal() {
        return tokenDecimal;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public BigDecimal getGranularity() {
        return granularity;
    }

    private static class TokenHoldersDTOBuilder{
        private String holderAddr;
        private String contractAddr;
        private String rawBalance;
        private BigDecimal tknBalance;
        private Integer tokenDecimal;
        private Long blockNumber;
        private BigDecimal granularity;

        public TokenHoldersDTOBuilder setHolderAddr(String holderAddr) {
            this.holderAddr = holderAddr;
            return this;
        }

        public TokenHoldersDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public TokenHoldersDTOBuilder setRawBalance(String rawBalance) {
            this.rawBalance = rawBalance;
            return this;
        }

        public TokenHoldersDTOBuilder setTknBalance(BigDecimal tknBalance) {
            this.tknBalance = tknBalance;
            return this;
        }

        public TokenHoldersDTOBuilder setTokenDecimal(Integer tokenDecimal) {
            this.tokenDecimal = tokenDecimal;
            return this;
        }

        public TokenHoldersDTOBuilder setBlockNumber(Long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public TokenHoldersDTOBuilder setGranularity(BigDecimal granularity) {
            this.granularity = granularity;
            return this;
        }
    }
}

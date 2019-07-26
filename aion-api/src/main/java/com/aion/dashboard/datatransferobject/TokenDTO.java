package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class TokenDTO {

    private String contractAddr;
    private String name;
    private String symbol;
    private String transactionHash;
    private String creatorAddress;
    private BigDecimal granularity;
    private BigDecimal totalSupply;
    private BigDecimal liquidSupply;
    private Long creationTimestamp;
    private Integer tokenDecimal;

    public TokenDTO(String contractAddr, String name, String symbol, String transactionHash, String creatorAddress, BigDecimal granularity, BigDecimal totalSupply, BigDecimal liquidSupply, Long creationTimestamp, Integer tokenDecimal) {
        this.contractAddr = contractAddr;
        this.name = name;
        this.symbol = symbol;
        this.transactionHash = transactionHash;
        this.creatorAddress = creatorAddress;
        this.granularity = granularity;
        this.totalSupply = totalSupply;
        this.liquidSupply = liquidSupply;
        this.creationTimestamp = creationTimestamp;
        this.tokenDecimal = tokenDecimal;
    }

    public String getContractAddr() {
        return contractAddr;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public String getCreatorAddress() {
        return creatorAddress;
    }

    public BigDecimal getGranularity() {
        return granularity;
    }

    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    public BigDecimal getLiquidSupply() {
        return liquidSupply;
    }

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }

    public Integer getTokenDecimal() {
        return tokenDecimal;
    }

    public static class TokenDTOBuilder{
        private String contractAddr;
        private String name;
        private String symbol;
        private String transactionHash;
        private String creatorAddress;
        private BigDecimal granularity;
        private BigDecimal totalSupply;
        private BigDecimal liquidSupply;
        private Long creationTimestamp;
        private Integer tokenDecimal;

        public TokenDTOBuilder setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
            return this;
        }

        public TokenDTOBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TokenDTOBuilder setSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public TokenDTOBuilder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public TokenDTOBuilder setCreatorAddress(String creatorAddress) {
            this.creatorAddress = creatorAddress;
            return this;
        }

        public TokenDTOBuilder setGranularity(BigDecimal granularity) {
            this.granularity = granularity;
            return this;
        }

        public TokenDTOBuilder setTotalSupply(BigDecimal totalSupply) {
            this.totalSupply = totalSupply;
            return this;
        }

        public TokenDTOBuilder setLiquidSupply(BigDecimal liquidSupply) {
            this.liquidSupply = liquidSupply;
            return this;
        }

        public TokenDTOBuilder setCreationTimestamp(Long creationTimestamp) {
            this.creationTimestamp = creationTimestamp;
            return this;
        }

        public TokenDTOBuilder setTokenDecimal(Integer tokenDecimal) {
            this.tokenDecimal = tokenDecimal;
            return this;
        }


        public TokenDTO createTokenDTO(){
            return new TokenDTO(contractAddr, name, symbol, transactionHash, creatorAddress, granularity, totalSupply, liquidSupply, creationTimestamp, tokenDecimal);
        }
    }



}

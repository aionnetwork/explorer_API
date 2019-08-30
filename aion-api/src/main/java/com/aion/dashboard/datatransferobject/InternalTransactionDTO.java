package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class InternalTransactionDTO {

    private static final ThreadLocal<Builder> builder = ThreadLocal.withInitial(Builder::new);
    private String nrgPrice;
    private String nrgLimit;
    private String data;
    private String type;
    private String fromAddr;
    private String toAddr;
    private String nonce;
    private String value;
    private long blockTimestamp;
    private long blockNumber;
    private String transactionHash;
    private boolean rejected;
    private int internalTransactionIndex;
    private String contractAddress;

    private InternalTransactionDTO(Builder builder){
        this.nrgPrice = builder.nrgPrice.toBigInteger().toString();
        this.nrgLimit = builder.nrgLimit.toBigInteger().toString();
        this.data = builder.data;
        this.type = builder.kind;
        this.fromAddr = builder.fromAddr;
        this.toAddr = builder.toAddr;
        this.nonce = builder.nonce.toBigInteger().toString();
        this.value = builder.value.toBigInteger().toString();
        this.blockNumber = builder.blockNumber;
        this.blockTimestamp = builder.blockTimestamp;
        this.transactionHash = builder.transactionHash;
        this.internalTransactionIndex = builder.internalTransactionIndex;
        this.rejected = builder.rejected;
        this.contractAddress = builder.contractAddress;
    }

    public static Builder builder() {
        return builder.get();
    }

    public String getNrgPrice() {
        return nrgPrice;
    }

    public String getNrgLimit() {
        return nrgLimit;
    }

    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public String getFromAddr() {
        return fromAddr;
    }

    public String getToAddr() {
        return toAddr;
    }

    public String getNonce() {
        return nonce;
    }

    public String getValue() {
        return value;
    }

    public long getBlockTimestamp() {
        return blockTimestamp;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public boolean getRejected() {
        return rejected;
    }

    public int getInternalTransactionIndex() {
        return internalTransactionIndex;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public static class Builder{
        private Builder(){}

        private BigDecimal nrgPrice;
        private BigDecimal nrgLimit;
        private String data;
        private String kind;
        private String fromAddr;
        private String toAddr;
        private BigDecimal nonce;
        private BigDecimal value;
        private long blockTimestamp;
        private long blockNumber;
        private String transactionHash;
        private boolean rejected;
        private int internalTransactionIndex;
        private String contractAddress;

        public Builder setNrgPrice(BigDecimal nrgPrice) {
            this.nrgPrice = nrgPrice;
            return this;
        }

        public Builder setNrgLimit(BigDecimal nrgLimit) {
            this.nrgLimit = nrgLimit;
            return this;
        }

        public Builder setData(String data) {
            this.data = data;
            return this;
        }

        public Builder setKind(String kind) {
            this.kind = kind;
            return this;
        }

        public Builder setFromAddr(String fromAddr) {
            this.fromAddr = fromAddr;
            return this;
        }

        public Builder setToAddr(String toAddr) {
            this.toAddr = toAddr;
            return this;
        }

        public Builder setNonce(BigDecimal nonce) {
            this.nonce = nonce;
            return this;
        }

        public Builder setValue(BigDecimal value) {
            this.value = value;
            return this;
        }

        public Builder setBlockTimestamp(long blockTimestamp) {
            this.blockTimestamp = blockTimestamp;
            return this;
        }

        public Builder setBlockNumber(long blockNumber) {
            this.blockNumber = blockNumber;
            return this;
        }

        public Builder setTransactionHash(String transactionHash) {
            this.transactionHash = transactionHash;
            return this;
        }

        public Builder setRejected(boolean rejected) {
            this.rejected = rejected;
            return this;
        }

        public Builder setInternalTransactionIndex(int internalTransactionIndex) {
            this.internalTransactionIndex = internalTransactionIndex;
            return this;
        }

        public Builder setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
            return this;
        }

        public InternalTransactionDTO build(){
            return new InternalTransactionDTO(this);
        }
    }
}

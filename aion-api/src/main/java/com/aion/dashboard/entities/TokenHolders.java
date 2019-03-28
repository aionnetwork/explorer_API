package com.aion.dashboard.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "TokenHolders")
@IdClass(TokenHolders.CompositeKey.class)
public class TokenHolders {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenHolders)) return false;
        TokenHolders that = (TokenHolders) o;
        return Objects.equals(getHolderAddr(), that.getHolderAddr()) &&
                Objects.equals(getContractAddr(), that.getContractAddr()) &&
                Objects.equals(getRawBalance(), that.getRawBalance()) &&
                Objects.equals(getTknBalance(), that.getTknBalance()) &&
                Objects.equals(getTokenDecimal(), that.getTokenDecimal()) &&
                Objects.equals(getBlockNumber(), that.getBlockNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHolderAddr(), getContractAddr(), getRawBalance(), getTknBalance(), getTokenDecimal(), getBlockNumber());
    }

    @Id
    private String holderAddr;
    @Id
    private String contractAddr;
    private String rawBalance;
    @Column(name = "scaled_balance")
    private BigDecimal tknBalance;
    private Integer tokenDecimal;
    private Long blockNumber;

    public String getContractAddr() {
        return contractAddr;
    }
    public String getHolderAddr() {
        return holderAddr;
    }
    public String getRawBalance() {
        return rawBalance;
    }

    @Transient @JsonGetter("tknBalance")
    public String getPlainStringTknBalance(){
        return tknBalance.toPlainString();
    }


    @JsonIgnore
    @Column(name = "scaled_balance")
    public BigDecimal getTknBalance() {
        return tknBalance;
    }
    public Integer getTokenDecimal() {
        return tokenDecimal;
    }
    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }
    public void setHolderAddr(String holderAddr) {
        this.holderAddr = holderAddr;
    }
    public void setRawBalance(String rawBalance) {
        this.rawBalance = rawBalance;
    }

    @Column(name = "scaled_balance")
    public void setTknBalance(BigDecimal tknBalance) {
        this.tknBalance = tknBalance;
    }
    public void setTokenDecimal(Integer tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
    }
    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public static class CompositeKey implements Serializable {
        private String holderAddr;
        private String contractAddr;

        public CompositeKey() {

        }

        public CompositeKey(String holderAddr, String contractAddr) {
            this.holderAddr = holderAddr;
            this.contractAddr = contractAddr;
        }

        public String getHolderAddr() {
            return holderAddr;
        }

        public void setHolderAddr(String holderAddr) {
            this.holderAddr = holderAddr;
        }

        public String getContractAddr() {
            return contractAddr;
        }

        public void setContractAddr(String contractAddr) {
            this.contractAddr = contractAddr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CompositeKey)) return false;
            CompositeKey that = (CompositeKey) o;
            return Objects.equals(getHolderAddr(), that.getHolderAddr()) &&
                    Objects.equals(getContractAddr(), that.getContractAddr());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getHolderAddr(), getContractAddr());
        }
    }
}

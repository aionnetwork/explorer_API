package com.aion.dashboard.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="Token")
public class Token {

    @Id
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
    private Integer year;
    private Integer month;
    private Integer day;

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
    @JsonIgnore
    public BigDecimal getGranularity() {
        return granularity;
    }
    @JsonIgnore
    public BigDecimal getTotalSupply() {
        return totalSupply;
    }
    @JsonIgnore
    public BigDecimal getLiquidSupply() {
        return liquidSupply;
    }

    @JsonGetter("granularity")
    @Transient
    public String getPlainStringGranularity(){return granularity.toPlainString();}
    @JsonGetter("totalSupply")
    @Transient
    public String getPlainStringTotalSupply(){return totalSupply.toPlainString();}
    @JsonGetter("liquidSupply")
    @Transient
    public String getPlainStringLiquidSupply(){return liquidSupply.toPlainString();}

    public Long getCreationTimestamp() {
        return creationTimestamp;
    }
    public Integer getTokenDecimal() {
        return tokenDecimal;
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

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
    public void setCreatorAddress(String creatorAddress) {
        this.creatorAddress = creatorAddress;
    }
    public void setGranularity(BigDecimal granularity) {
        this.granularity = granularity;
    }
    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }
    public void setLiquidSupply(BigDecimal liquidSupply) {
        this.liquidSupply = liquidSupply;
    }
    public void setCreationTimestamp(Long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
    public void setTokenDecimal(Integer tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
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
}
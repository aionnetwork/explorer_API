package com.aion.dashboard.downloads.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "TokenTransfers")
public class TokenTransfers {

    @Id
    private Long transferId;
    private Long blockNumber;
    private Long transferTimestamp;
    private String transactionHash;
    private String toAddr;
    private String fromAddr;
    private String rawValue;
    private String contractAddr;
    private String operatorAddr;
    @Column(name = "scaled_value")
    private BigDecimal tknValue;
    private Integer tokenDecimal;
    private Integer year;
    private Integer month;
    private Integer day;

    public Long getTransferId() {
        return transferId;
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
    public BigDecimal getTknValue() {
        return tknValue;
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

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }
    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }
    public void setTransferTimestamp(Long transferTimestamp) {
        this.transferTimestamp = transferTimestamp;
    }
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
    public void setToAddr(String toAddr) {
        this.toAddr = toAddr;
    }
    public void setFromAddr(String fromAddr) {
        this.fromAddr = fromAddr;
    }
    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }
    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }
    public void setOperatorAddr(String operatorAddr) {
        this.operatorAddr = operatorAddr;
    }
    public void setTknValue(BigDecimal tknValue) {
        this.tknValue = tknValue;
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

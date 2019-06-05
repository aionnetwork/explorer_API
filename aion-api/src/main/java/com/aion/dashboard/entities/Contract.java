package com.aion.dashboard.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Contract")
public class Contract {

    @Id
    private String contractAddr;
    private String contractName;
    private String contractTxHash;
    private String contractCreatorAddr;
    private Long deployTimestamp;
    private Long blockNumber;
    private Integer year;
    private Integer month;
    private Integer day;
    private String type;

    public String getContractAddr() {
        return contractAddr;
    }
    public String getContractName() {
        return contractName;
    }
    public String getContractTxHash() {
        return contractTxHash;
    }
    public String getContractCreatorAddr() {
        return contractCreatorAddr;
    }
    public Long getDeployTimestamp() {
        return deployTimestamp;
    }
    public Long getBlockNumber() {
        return blockNumber;
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
    public String getType() {
        return type;
    }

    public void setContractAddr(String contractAddr) {
        this.contractAddr = contractAddr;
    }
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    public void setContractTxHash(String contractTxHash) {
        this.contractTxHash = contractTxHash;
    }
    public void setContractCreatorAddr(String contractCreatorAddr) {
        this.contractCreatorAddr = contractCreatorAddr;
    }
    public void setDeployTimestamp(Long deployTimestamp) {
        this.deployTimestamp = deployTimestamp;
    }
    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
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
    public Contract setType(String type) {
        this.type = type;
        return this;
    }
}

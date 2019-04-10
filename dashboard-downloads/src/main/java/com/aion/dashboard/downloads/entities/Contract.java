package com.aion.dashboard.downloads.entities;

import org.json.JSONObject;

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

    public static Contract toObject(JSONObject jsonObject) {
        Contract contract = new Contract();

        contract.setBlockNumber(jsonObject.getLong("blockNumber"));
        contract.setContractAddr(jsonObject.getString("contractAddr"));
        contract.setContractName(jsonObject.getString("contractName"));
        contract.setContractTxHash(jsonObject.getString("contractTxHash"));
        contract.setDeployTimestamp(jsonObject.getLong("deployTimestamp"));
        contract.setContractCreatorAddr(jsonObject.getString("contractCreatorAddr"));

        contract.setYear(jsonObject.getInt("year"));
        contract.setMonth(jsonObject.getInt("month"));
        contract.setDay(jsonObject.getInt("day"));

        return contract;
    }
}

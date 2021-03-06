package com.aion.dashboard.downloads.entities;

import org.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="Transaction")
public class Transaction {

    @Id
    private String transactionHash;
    private String blockHash;
    @Column(name = "block_number")
    private Long blockNumber;
    @Column(name = "block_timestamp")
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


    @Column(name = "block_number")
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
    @Column(name = "block_number")
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

    public static Transaction toObject(JSONObject jsonObject) {
        Transaction transaction = new Transaction();

        transaction.setData(jsonObject.getString("data"));
        transaction.setNonce(jsonObject.getString("nonce"));
        transaction.setValue(new BigDecimal(jsonObject.getLong("value")));
        transaction.setToAddr(jsonObject.getString("toAddr"));
        transaction.setNrgPrice(jsonObject.getLong("nrgPrice"));
        transaction.setTxError(jsonObject.getString("txError"));
        transaction.setFromAddr(jsonObject.getString("fromAddr"));
        transaction.setBlockHash(jsonObject.getString("blockHash"));
        transaction.setNrgConsumed(jsonObject.getLong("nrgConsumed"));
        transaction.setContractAddr(jsonObject.getString("contractAddr"));
        transaction.setTransactionLog(jsonObject.getString("transactionLog"));
        transaction.setTransactionHash(jsonObject.getString("transactionHash"));
        transaction.setTransactionIndex(jsonObject.getLong("transactionIndex"));
        transaction.setBlockTimestamp(jsonObject.getLong("blockTimestamp"));
        transaction.setTransactionTimestamp(jsonObject.getLong("transactionTimestamp"));

        transaction.setYear(jsonObject.getInt("year"));
        transaction.setMonth(jsonObject.getInt("month"));
        transaction.setDay(jsonObject.getInt("day"));

        return transaction;
    }
}

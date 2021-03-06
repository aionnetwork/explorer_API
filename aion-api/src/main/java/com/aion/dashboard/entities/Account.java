package com.aion.dashboard.entities;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="Account")
public class Account {

    @Id
    private String address;
    private String transactionHash;
    private String nonce;
    private Long lastBlockNumber;
    private BigDecimal balance;
    private Boolean contract;

    public String getAddress() {
        return address;
    }
    public String getTransactionHash() {
        return transactionHash;
    }
    public String getNonce() {
        return nonce;
    }
    public Long getLastBlockNumber() {
        return lastBlockNumber;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public Boolean isContract() {
        return contract;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }
    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
    public void setLastBlockNumber(Long lastBlockNumber) {
        this.lastBlockNumber = lastBlockNumber;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public void setContract(Boolean contract) {
        this.contract = contract;
    }
}
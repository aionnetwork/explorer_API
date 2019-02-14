package com.aion.dashboard.entities;

import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="Balance")
public class Balance {

    @Id
    private String address;
    private Long nonce;
    private Long lastBlockNumber;
    private Long transactionId;
    private BigDecimal balance;
    private Boolean contract;

    public String getAddress()       { return address;         }
    public Long getNonce()           { return nonce;           }
    public Long getLastBlockNumber() { return lastBlockNumber; }
    public Long getTransactionId()   { return transactionId;   }
    public BigDecimal getBalance()   { return balance;         }
    public Boolean isContract()      { return contract;        }

    public void setAddress(String address)               { this.address         = address;         }
    public void setNonce(Long nonce)                     { this.nonce           = nonce;           }
    public void setLastBlockNumber(Long lastBlockNumber) { this.lastBlockNumber = lastBlockNumber; }
    public void setTransactionId(Long transactionId)     { this.transactionId   = transactionId;   }
    public void setBalance(BigDecimal balance)           { this.balance         = balance;         }
    public void setContract(Boolean contract)            { this.contract        = contract;        }

    public static Balance toObject(JSONObject jsonObject) {
        Balance balance = new Balance();

        balance.setLastBlockNumber(jsonObject.getLong("lastBlockNumber"));
        balance.setTransactionId(jsonObject.getLong("transactionId"));
        balance.setContract(jsonObject.getBoolean("contract"));
        balance.setAddress(jsonObject.getString("address"));
        balance.setBalance(new BigDecimal(jsonObject.getLong("balance")));
        balance.setNonce(jsonObject.getLong("nonce"));

        return balance;
    }
}
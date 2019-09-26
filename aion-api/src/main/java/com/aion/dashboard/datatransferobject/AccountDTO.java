package com.aion.dashboard.datatransferobject;

import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.annotation.JsonGetter;
import java.math.BigDecimal;
import java.math.BigInteger;

public class AccountDTO {

    private final String address;
    private final String nonce;
    private final BigDecimal balance;
    private final boolean isContract;

    public AccountDTO(String address, String nonce, BigDecimal balance, boolean isContract) {
        this.address = address;
        this.nonce = nonce;
        this.balance = balance;
        this.isContract = isContract;
    }

    public String getAddress() {
        return address;
    }

    public Long getNonce() {
        return new BigInteger(nonce, 16).longValue();
    }

    public String getBalance() {
        return Utility.toAion(balance);
    }

    @JsonGetter("contract")
    public boolean isContract() {
        return isContract;
    }
}

package com.aion.dashboard.datatransferobject;

import java.math.BigDecimal;

public class AccountDTO {

    private String address;
    private String nonce;
    private BigDecimal balance;
    private Boolean contract;

    private AccountDTO(String address, String nonce, BigDecimal balance, Boolean contract) {
        this.address = address;
        this.nonce = nonce;
        this.balance = balance;
        this.contract = contract;
    }

    public String getAddress() {
        return address;
    }

    public String getNonce() {
        return nonce;
    }

    public String getBalance() {
        return balance.toPlainString();
    }

    public Boolean getContract() {
        return contract;
    }



    public static class AccountDTOBuilder{
        private String address;
        private String nonce;
        private BigDecimal balance;
        private Boolean contract;

        public AccountDTOBuilder setContract(Boolean contract) {
            this.contract = contract;
            return this;
        }

        public AccountDTOBuilder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public AccountDTOBuilder setNonce(String nonce) {
            this.nonce = nonce;
            return this;
        }

        public AccountDTOBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public AccountDTO createAccountDTO(){
            return new AccountDTO(address, nonce, balance, contract);
        }

    }
}

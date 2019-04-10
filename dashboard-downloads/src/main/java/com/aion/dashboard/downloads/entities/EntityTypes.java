package com.aion.dashboard.downloads.entities;

public enum EntityTypes {
    BLOCK_TXNS_CSV("Block-Transactions"),
    BLOCK_DETAILS_CSV("Block-Details"),

    TOKEN_HDRS_CSV("Token-Holders"),
    TOKEN_TXFS_CSV("Token-Transfers"),
    TOKEN_DETAILS_CSV("Token-Details"),

    CONTRACT_EVTS_CSV("Contract-Events"),
    CONTRACT_TXNS_CSV("Contract-Transactions"),
    CONTRACT_DETAILS_CSV("Contract-Details"),

    ACCOUNT_BLKS_CSV("Account-Blocks"),
    ACCOUNT_TKNS_CSV("Account-Tokens"),
    ACCOUNT_TXNS_CSV("Account-Transactions"),
    ACCOUNT_ITXFS_CSV("Account-Internal-Transfers"),
    ACCOUNT_DETAILS_CSV("Account-Details"),

    TRANSACTION_DETAILS_CSV("Transaction-Details"),
    ITRANSFER_DETAILS_CSV("Internal-Transfer-Details");

    private String string;

    @Override
    public String toString() {
        return this.string;
    }

    EntityTypes(String string) {
        this.string = string;
    }
}

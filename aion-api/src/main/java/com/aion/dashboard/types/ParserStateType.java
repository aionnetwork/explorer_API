package com.aion.dashboard.types;

public enum ParserStateType {
    HEAD_BLOCK_TABLE(1),
    HEAD_BLOCKCHAIN(2),
    HEAD_INTEGRITY(3),
    HEAD_GRAPHING(4),
    VALIDATOR_STATS(10),
    TRANSACTION_STATS(11);

    private int id;
    ParserStateType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ParserStateType fromId(int id) {
        switch (id) {
            case 1: return HEAD_BLOCK_TABLE;
            case 2: return HEAD_BLOCKCHAIN;
            case 3: return HEAD_INTEGRITY;
            case 4: return HEAD_GRAPHING;
            case 10: return VALIDATOR_STATS;
            case 11: return TRANSACTION_STATS;
        }
        return null;
    }
}

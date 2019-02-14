package com.aion.dashboard.downloads.types;

public enum ParserStateType {
    HEAD_BLOCK_TABLE(1),
    HEAD_BLOCKCHAIN(2),
    HEAD_INTEGRITY(3);

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
            default: return null;
        }
    }
}
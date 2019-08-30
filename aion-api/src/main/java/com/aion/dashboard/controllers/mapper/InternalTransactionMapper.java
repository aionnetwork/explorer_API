package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.InternalTransactionDTO;
import com.aion.dashboard.entities.InternalTransaction;

public class InternalTransactionMapper extends Mapper<InternalTransaction, InternalTransactionDTO> {

    private static final InternalTransactionMapper mapper = new InternalTransactionMapper();
    public static InternalTransactionMapper getInstance() {
        return mapper;
    }
    @Override
    InternalTransactionDTO makeDTO(InternalTransaction internalTransaction) {
        return InternalTransactionDTO.builder()
                .setBlockNumber(internalTransaction.getBlockNumber())
                .setBlockTimestamp(internalTransaction.getTimestamp())
                .setContractAddress(internalTransaction.getContractAddress())
                .setData(internalTransaction.getData())
                .setFromAddr(internalTransaction.getFromAddr())
                .setInternalTransactionIndex(internalTransaction.getInternalTransactionIndex())
                .setKind(internalTransaction.getKind())
                .setNonce(internalTransaction.getNonce())
                .setNrgLimit(internalTransaction.getNrgLimit())
                .setNrgPrice(internalTransaction.getNrgPrice())
                .setRejected(internalTransaction.isRejected())
                .setToAddr(internalTransaction.getToAddr())
                .setTransactionHash(internalTransaction.getTransactionHash())
                .setValue(internalTransaction.getValue())
                .build();
    }
}

package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TransactionDTO;
import com.aion.dashboard.entities.Transaction;

public class TransactionMapper extends Mapper<Transaction, TransactionDTO> {


    private static final TransactionMapper mapper = new TransactionMapper();
    private TransactionMapper(){}

    public static TransactionMapper getInstance() {
        return mapper;
    }

    public static TransactionDTO makeTransactionDTO(Transaction transaction)
    {
        TransactionDTO.TransactionDTOBuilder transactionDTOBuilder = TransactionDTO.newBuilder().
                setTransactionHash(transaction.getTransactionHash())
                .setTransactionIndex(transaction.getTransactionIndex())
                .setTransactionLog(transaction.getTransactionLog())
                .setTransactionTimestamp(transaction.getTransactionTimestamp())
                .setBlockHash(transaction.getBlockHash())
                .setBlockNumber(transaction.getBlockNumber())
                .setContractAddr(transaction.getContractAddr())
                .setData(transaction.getData())
                .setDay(transaction.getDay())
                .setFromAddr(transaction.getFromAddr())
                .setMonth(transaction.getMonth())
                .setNonce(transaction.getNonce())
                .setNrgConsumed(transaction.getNrgConsumed())
                .setToAddr(transaction.getToAddr())
                .setTxError(transaction.getTxError())
                .setNrgPrice(transaction.getNrgPrice())
                .setType(transaction.getType())
                .setValue(transaction.getValue())
                .setYear(transaction.getYear())
                .setBlockTimestamp(transaction.getBlockTimestamp());

        return transactionDTOBuilder.createTransactionDTO();
    }

    @Override
    public TransactionDTO makeDTO(Transaction transaction) {
        return makeTransactionDTO(transaction);
    }
}

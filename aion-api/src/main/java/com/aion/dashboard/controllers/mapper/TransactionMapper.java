package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TransactionDTO;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.view.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.stream.Collectors;

public class TransactionMapper {


    /**
     * @param transaction transaction object
     * @return A  Data Transfer Object of transaction
     */
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


    /**
     * @param transaction transaction in a Page object
     * @return A  List of Data Transfer Object of transaction
     */
    public static Result<TransactionDTO> makeTransactionDTOList(Page<Transaction> transaction)
    {

        return Result.from(transaction.getContent().stream()
                .map(TransactionMapper::makeTransactionDTO)
                .collect(Collectors.toList()), transaction);

    }


}

package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TransactionDTO;
import com.aion.dashboard.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionMapper {



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


    public static Page<TransactionDTO> makeTransactionDTOList(Page<Transaction> transaction)
    {
        return new PageImpl<TransactionDTO>( transaction.getContent().stream()
                .map(TransactionMapper::makeTransactionDTO)
                .collect(Collectors.toList()),transaction.getPageable(),transaction.getTotalElements());
    }


}

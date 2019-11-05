package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TransactionStatsDTO;
import com.aion.dashboard.entities.TransactionStats;

public class TransactionStatsMapper extends Mapper<TransactionStats, TransactionStatsDTO> {

    private static final TransactionStatsMapper mapper=new TransactionStatsMapper();

    public static TransactionStatsMapper getMapper() {
        return mapper;
    }

    @Override
    TransactionStatsDTO makeDTO(TransactionStats transactionStats) {
        return new TransactionStatsDTO(transactionStats.getNumberOfTransaction(),
            transactionStats.getBlockNumber(),
            transactionStats.getBlockTimestamp(),
            transactionStats.getNumberOfActiveAddresses(),
            transactionStats.getTotalTransferred());
    }
}

package com.aion.dashboard.services;

import com.aion.dashboard.entities.TxLog;
import com.aion.dashboard.repositories.TxLogJpaRepository;
import com.aion.dashboard.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TxLogService {


    private TxLogJpaRepository txLogJpaRepository;

    private Sort sort = new Sort( Sort.Direction.DESC, "blockNumber");

    @Autowired
    public TxLogService(TxLogJpaRepository txLogJpaRepository) {
        this.txLogJpaRepository = txLogJpaRepository;
    }

    public List<TxLog> findLogsForTransaction(String transactionHash){
        return txLogJpaRepository.findAllByTransactionHash(Utility.sanitizeHex(transactionHash));
    }

    public Page<TxLog> findLogsForBlock(long blockNumber, int page, int size){
        return txLogJpaRepository.findAllByBlockNumber(blockNumber, PageRequest.of(page,size, sort));
    }

    public Page<TxLog> findLogsForContract(String contractAddress, int page, int size){
        return txLogJpaRepository.findAllByContractAddr(Utility.sanitizeHex(contractAddress), PageRequest.of(page,size, sort));
    }

    public Page<TxLog> findLogsForContractAndInTimeRange(String contractAddress, long start, long end, int page, int size){
        return txLogJpaRepository.findAllByContractAddrAndBlockTimestampBetween(Utility.sanitizeHex(contractAddress), start,end,PageRequest.of(page,size, sort));
    }

    public Page<TxLog> findLogsForContractAndInBlockRange(String contractAddress, long start, long end, int page, int size){
        return txLogJpaRepository.findAllByContractAddrAndBlockNumberBetween(Utility.sanitizeHex(contractAddress), start, end,PageRequest.of(page,size, sort));
    }

    public Page<TxLog> findLogsInTimeRange(long start, long end, int page, int size){
        return txLogJpaRepository.findAllByBlockTimestampBetween( start,end,PageRequest.of(page,size, sort));
    }

    public Page<TxLog> findLogsForBlockRange(long start, long end, int page, int size){
        return txLogJpaRepository.findAllByBlockNumberBetween(start, end,PageRequest.of(page,size, sort));
    }
}


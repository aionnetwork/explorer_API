package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.TxLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TxLogJpaRepository extends PagingAndSortingRepository<TxLog, TxLog.CompositeKey> {

    List<TxLog> findAllByTransactionHash(String txHash);

    Page<TxLog> findAllByBlockNumber(long blockNumber,Pageable pageable);

    Page<TxLog> findAllByContractAddr(String contractAddr, Pageable pageable);

    Page<TxLog> findAllByContractAddrAndBlockTimestampBetween(String contractAddr, long start, long end, Pageable pageable);

    Page<TxLog> findAllByContractAddrAndBlockNumberBetween(String contractAddr, long start, long end, Pageable pageable);


    Page<TxLog> findAllByBlockTimestampBetween(long start, long end, Pageable pageable);

    Page<TxLog> findAllByBlockNumberBetween(long start, long end, Pageable pageable);
}

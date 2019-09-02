package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.InternalTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface InternalTransactionJpaRepository extends PagingAndSortingRepository<InternalTransaction, InternalTransaction.Composite> {
    List<InternalTransaction> findAllByTransactionHash(String txHash);
    Page<InternalTransaction> findAllByBlockNumber(long blockNumber, Pageable pageable);
    Page<InternalTransaction> findAllByContractAddress(String contractAddress, Pageable pageable);
    Page<InternalTransaction> findAllByToAddrOrFromAddr(String toAddr, String fromAddr, Pageable pageable);
    Boolean existsByToAddrOrFromAddr(String toAddr, String fromAddr);
    default Boolean existsByAddr(String addr){
        return existsByToAddrOrFromAddr(addr, addr);
    }
}

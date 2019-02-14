package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.InternalTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InternalTransferJpaRepository extends PagingAndSortingRepository<InternalTransfer, InternalTransfer.CompositeKey> {


    Page<InternalTransfer> findAllByTransactionHash(String transactionHash, Pageable pageable);
    Page<InternalTransfer> findAllByToAddrOrFromAddr(String toAddr, String fromAddr, Pageable pageable);
    Page<InternalTransfer> findAllByBlockTimestampBetween(long start, long end, Pageable pageable);

    InternalTransfer findTopByToAddrOrFromAddr(String toAddr, String fromAddr);
}

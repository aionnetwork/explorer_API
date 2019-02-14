package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.InternalTransfer;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface InternalTransferJpaRepository extends PagingAndSortingRepository<InternalTransfer, InternalTransfer.CompositeKey> {

        List<InternalTransfer> findAllByToAddrOrFromAddr(String toAddr, String fromAddr);
        List<InternalTransfer> findAllByTransactionHash(String transactionHash);
        List<InternalTransfer> findAllByTimestampBetween(long start, long end);
}

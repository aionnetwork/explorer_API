package com.aion.dashboard.services;

import com.aion.dashboard.entities.InternalTransfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InternalTransferService {

    Page<InternalTransfer> findAllByTransactionHash(String transactionHash, Pageable pageable);
    Page<InternalTransfer> findAllByToAddrOrFromAddr(String toAddr, String fromAddr, Pageable pageable);
    Page<InternalTransfer> findAllByBlockTimestampBetween(long start, long end, Pageable pageable);

    InternalTransfer findTopByToAddrOrFromAddr(String toAddr, String fromAddr);
}

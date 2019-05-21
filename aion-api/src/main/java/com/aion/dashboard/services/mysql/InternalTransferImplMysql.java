package com.aion.dashboard.services.mysql;

import com.aion.dashboard.entities.InternalTransfer;
import com.aion.dashboard.repositories.InternalTransferJpaRepository;
import com.aion.dashboard.services.InternalTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;


@Component
public class InternalTransferImplMysql implements InternalTransferService {
    @Autowired
    private InternalTransferJpaRepository internalTransferJpaRepository;


    @Override
    public Page<InternalTransfer> findAllByTransactionHash(String transactionHash, Pageable pageable) {
        return internalTransferJpaRepository.findAllByTransactionHash(transactionHash, pageable) ;
    }

    @Override
    public Page<InternalTransfer> findAllByToAddrOrFromAddr(String toAddr, String fromAddr, Pageable pageable) {
        return internalTransferJpaRepository.findAllByToAddrOrFromAddr(toAddr, fromAddr, pageable) ;
    }

    @Override
    public Page<InternalTransfer> findAllByBlockTimestampBetween(long start, long end, Pageable pageable) {
        return internalTransferJpaRepository.findAllByBlockTimestampBetween(start, end, pageable) ;
    }

    @Override
    public InternalTransfer findTopByToAddrOrFromAddr(String toAddr, String fromAddr) {
        return internalTransferJpaRepository.findTopByToAddrOrFromAddr(toAddr, fromAddr);
    }
}

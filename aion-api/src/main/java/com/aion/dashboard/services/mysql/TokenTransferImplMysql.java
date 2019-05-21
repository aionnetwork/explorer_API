package com.aion.dashboard.services.mysql;

import com.aion.dashboard.entities.TokenTransfers;
import com.aion.dashboard.repositories.TokenTransfersJpaRepository;
import com.aion.dashboard.services.TokenTransfersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenTransferImplMysql implements TokenTransfersService {

    @Autowired
    private TokenTransfersJpaRepository tokenTransfersJpaRepository;

    @Override
    public Page<TokenTransfers> findByYearAndMonthBetweenAndTransferTimestampBetweenAndContractAddr(int year, int startMonth, int endMonth, long start, long end, String contractAddr, Pageable pageable) {
        return tokenTransfersJpaRepository.findByYearAndMonthBetweenAndTransferTimestampBetweenAndContractAddr(year, startMonth, endMonth, start, end, contractAddr, pageable) ;
    }

    @Override
    public Page<TokenTransfers> findByYearBetweenAndTransferTimestampBetweenAndContractAddr(int startYear, int endYear, long start, long end, String contractAddr, Pageable pageable) {
        return tokenTransfersJpaRepository.findByYearBetweenAndTransferTimestampBetweenAndContractAddr(startYear, endYear, start, end, contractAddr, pageable) ;
    }

    @Override
    public List<TokenTransfers> findByContractAddr(String contractAddr) {
        return tokenTransfersJpaRepository.findByContractAddr(contractAddr);
    }

    @Override
    public Long countByContractAddr(String contractAddr) {
        return tokenTransfersJpaRepository.countByContractAddr(contractAddr) ;
    }
}

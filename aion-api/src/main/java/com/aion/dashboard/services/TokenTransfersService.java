package com.aion.dashboard.services;

import com.aion.dashboard.entities.TokenTransfers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TokenTransfersService {

    Page<TokenTransfers> findByYearAndMonthBetweenAndTransferTimestampBetweenAndContractAddr(int year, int startMonth, int endMonth, long start, long end, String contractAddr, Pageable pageable);
    Page<TokenTransfers> findByYearBetweenAndTransferTimestampBetweenAndContractAddr(int startYear, int endYear, long start, long end, String contractAddr, Pageable pageable);
    List<TokenTransfers> findByContractAddr(String contractAddr);
    Long countByContractAddr(String contractAddr);
}

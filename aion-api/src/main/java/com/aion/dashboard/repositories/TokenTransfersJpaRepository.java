package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.TokenTransfers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenTransfersJpaRepository extends PagingAndSortingRepository<TokenTransfers, Long> , JpaSpecificationExecutor<TokenTransfers> {
    Page<TokenTransfers> findByYearAndMonthBetweenAndTransferTimestampBetweenAndContractAddr(int year, int startMonth, int endMonth, long start, long end, String contractAddr, Pageable pageable);
    Page<TokenTransfers> findByYearBetweenAndTransferTimestampBetweenAndContractAddr(int startYear, int endYear, long start, long end, String contractAddr, Pageable pageable);
    List<TokenTransfers> findByContractAddr(String contractAddr);
    Long countByContractAddr(String contractAddr);

}

package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.TokenTransfers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenTransfersJpaRepository extends PagingAndSortingRepository<TokenTransfers, Long>, JpaSpecificationExecutor<TokenTransfers> {
    Page<TokenTransfers> findByYearBetweenAndTransferTimestampBetweenAndContractAddr(int yearStart, int yearEnd, long timestampStart, long timestampEnd, String contractAddr, Pageable pageable);
    Page<TokenTransfers> findByYearAndMonthBetweenAndTransferTimestampBetweenAndContractAddr(int year, int monthStart, int monthEnd, long timestampStart, long timestampEnd, String contractAddr, Pageable pageable);
}

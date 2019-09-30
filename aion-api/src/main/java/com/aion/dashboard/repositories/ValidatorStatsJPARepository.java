package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.ValidatorStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ValidatorStatsJPARepository extends PagingAndSortingRepository<ValidatorStats, ValidatorStats.CompositeKey> {

    Page<ValidatorStats> findAllByBlockNumber(long BlockNumber, Pageable pageable);
    Page<ValidatorStats> findAllByBlockNumberAndSealType(long blockNumber, String sealType, Pageable pageable);
}

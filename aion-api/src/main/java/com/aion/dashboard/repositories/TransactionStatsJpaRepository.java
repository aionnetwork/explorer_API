package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.TransactionStats;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionStatsJpaRepository extends PagingAndSortingRepository<TransactionStats, Long> {
    List<TransactionStats> findAll();
}

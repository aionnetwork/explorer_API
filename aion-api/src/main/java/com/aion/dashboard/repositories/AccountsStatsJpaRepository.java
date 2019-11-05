package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.AccountStats;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountsStatsJpaRepository extends PagingAndSortingRepository<AccountStats, AccountStats.Composite> {
    List<AccountStats> findAll();
    List<AccountStats> findByBlockNumber(long blockNumber);
}

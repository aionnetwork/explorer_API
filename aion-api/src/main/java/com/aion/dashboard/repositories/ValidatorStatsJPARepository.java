package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.ValidatorStats;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ValidatorStatsJPARepository extends PagingAndSortingRepository<ValidatorStats, ValidatorStats.CompositeKey> {

    List<ValidatorStats> findAllByBlockNumber(long BlockNumber);
}

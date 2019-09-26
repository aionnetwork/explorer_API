package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Metrics;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetricsJpaRepository extends PagingAndSortingRepository<Metrics, Metrics.CompositeKey> {
}

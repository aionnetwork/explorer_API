package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Metrics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricsJpaRepository extends PagingAndSortingRepository<Metrics, Long> {
    @Query(value = "SELECT * FROM metrics WHERE start_block IS NOT NULL AND id = ?1", nativeQuery = true)
    Optional<Metrics> findById(int id);
}

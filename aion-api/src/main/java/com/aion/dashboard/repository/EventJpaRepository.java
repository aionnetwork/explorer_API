package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJpaRepository extends PagingAndSortingRepository<Event, Long> {
    Page<Event> findByTransactionId(Long transaction_id, Pageable pageable);
    @Query(value = "SELECT * FROM event WHERE contract_addr = ?1 ORDER BY event_timestamp DESC", nativeQuery = true)
    Page<Event> findByContractAddr(String contract_addr, Pageable pageable);
    @Query(value = "SELECT COUNT(*) FROM event WHERE event_timestamp >= ?1 AND event_timestamp <= ?2", nativeQuery = true)
    Long getCountBetween(Long start, Long end);
}

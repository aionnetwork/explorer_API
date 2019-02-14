package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface EventJpaRepository extends PagingAndSortingRepository<Event, Long> {
    Page<Event> findByTransactionHash(String transactionHash, Pageable pageable);
    @Query(value = "SELECT * FROM event WHERE contract_addr = ?1 ORDER BY event_timestamp DESC", nativeQuery = true)
    Page<Event> findByContractAddr(String contractAddr, Pageable pageable);
}

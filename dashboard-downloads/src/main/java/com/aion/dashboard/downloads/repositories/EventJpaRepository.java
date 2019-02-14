package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventJpaRepository extends PagingAndSortingRepository<Event, Long> {
    @Query(value = "SELECT * FROM event WHERE event_id >= ?1 AND event_id <= ?2 AND contract_addr = ?3 ORDER BY event_timestamp DESC", nativeQuery = true)
    List<Event> findAllByContractAddrBetweenOrderByBlockNumberDesc(Long start, Long end, String contractAddr);
}

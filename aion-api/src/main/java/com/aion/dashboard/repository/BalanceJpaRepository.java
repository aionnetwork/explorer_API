package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceJpaRepository extends PagingAndSortingRepository<Balance, Long> {
    @Query(value = "SELECT * FROM balance WHERE contract = FALSE", nativeQuery = true)
    Page<Balance> getRichList(Pageable pageable);
    @Query(value = "SELECT * FROM balance WHERE address = ?1", nativeQuery = true)
    Balance findByAddress(String address);
}
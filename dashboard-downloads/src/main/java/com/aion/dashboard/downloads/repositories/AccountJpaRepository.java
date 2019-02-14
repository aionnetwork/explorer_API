package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.Account;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface AccountJpaRepository extends PagingAndSortingRepository<Account, Long> {
    @Query(value = "SELECT * FROM balance WHERE address = ?1", nativeQuery = true)
    Account findByAddress(String address);
}
package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface AccountJpaRepository extends PagingAndSortingRepository<Account, Long> {
    @Query(value = "SELECT * FROM account WHERE contract = FALSE", nativeQuery = true)
    Page<Account> getRichList(Pageable pageable);
    @Query(value = "SELECT * FROM account WHERE address = ?1", nativeQuery = true)
    Account findByAddress(String address);
}
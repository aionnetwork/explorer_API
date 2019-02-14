package com.aion.dashboard.repository;

import com.aion.dashboard.entities.TransactionMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMapJpaRepository extends PagingAndSortingRepository<TransactionMap, String> {
}
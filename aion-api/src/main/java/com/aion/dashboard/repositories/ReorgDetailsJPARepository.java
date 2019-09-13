package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.ReorgDetails;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReorgDetailsJPARepository extends PagingAndSortingRepository<ReorgDetails, Long> {
}

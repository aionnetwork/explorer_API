package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractJpaRepository extends PagingAndSortingRepository<Contract, Long> {
    Contract findByContractAddr(String contractAddr);

    Page<Contract> findByYearAndMonthBetweenAndDeployTimestampBetween(int year,int startMonth, int endMonth, long start, long end, Pageable pageable);
    Page<Contract> findByYearBetweenAndDeployTimestampBetween(int startYear, int endYear, long start, long end, Pageable pageable);

}

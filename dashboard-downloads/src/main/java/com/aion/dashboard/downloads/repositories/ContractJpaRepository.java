package com.aion.dashboard.downloads.repositories;


import com.aion.dashboard.downloads.entities.Contract;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractJpaRepository extends PagingAndSortingRepository<Contract, Long> {
    Contract findByContractAddr(String contractAddr);
}

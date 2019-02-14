package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Contract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractJpaRepository extends PagingAndSortingRepository<Contract, Long> {
    @Query(value = "SELECT * FROM contract ORDER BY deploy_timestamp DESC", nativeQuery = true)
    Page<Contract> findAllContracts(Pageable pageable);
    Contract findByContractAddr(String contract_addr);
}

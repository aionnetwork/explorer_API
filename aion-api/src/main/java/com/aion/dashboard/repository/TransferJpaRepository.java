package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferJpaRepository extends PagingAndSortingRepository<Transfer, Long> {
    @Query(value = "SELECT * FROM transfer WHERE contract_addr = ?1 AND (to_addr = ?2 OR from_addr = ?2) ORDER BY transfer_timestamp DESC", nativeQuery = true)
    Page<Transfer> findTransfersByContractAndHolder(String contract_addr, String address, Pageable pageable);
    @Query(value = "SELECT * FROM transfer WHERE contract_addr = ?1 ORDER BY transfer_timestamp DESC", nativeQuery = true)
    Page<Transfer> getLatestTransfersByContractAddr(String contract_addr, Pageable pageable);
    @Query(value = "SELECT COUNT(contract_addr) FROM transfer WHERE contract_addr = ?1", nativeQuery = true)
    Long getTotalTransfers(String contract_addr);
}

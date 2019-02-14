package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionJpaRepository extends PagingAndSortingRepository<Transaction,Long>, JpaSpecificationExecutor<Transaction> {
	@Query(value = "select * from transaction where id = (select id from transaction_map where transaction_hash = ?1)", nativeQuery = true)
	Transaction getTransactionByTransactionHash(String transactionHash);

	Transaction findByTransactionHash(String transactionHash);
	Transaction findByContractAddr(String contractAddr);
}
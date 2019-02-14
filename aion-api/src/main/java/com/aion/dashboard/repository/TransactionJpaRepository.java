package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionJpaRepository extends PagingAndSortingRepository<Transaction,Long> {
	Page<Transaction> findByIdBetween(Long startId, Long endId, Pageable pageable);
	@Query(value = "select * from transaction where id = (select id from transaction_map where transaction_hash = ?1)", nativeQuery = true)
	Transaction getTransactionByTransactionHash(String transactionHash);
	@Query(value = "SELECT * FROM transaction WHERE id >= ?1 AND id <= ?2 AND (from_addr = ?3 OR to_addr = ?3) ORDER BY block_number DESC", nativeQuery = true)
	Page<Transaction> getTransactionsForAddressBetween(Long startId, Long endId, String address, Pageable pageable);
	@Query(value = "SELECT * FROM transaction WHERE from_addr = ?1 OR to_addr = ?1 ORDER BY block_number DESC", nativeQuery = true)
	Page<Transaction> findTransactionsByAddress(String address, Pageable pageable);
	@Query(value = "SELECT * FROM transaction WHERE from_addr = ?1 OR to_addr = ?1 ORDER BY block_number DESC", nativeQuery = true)
	List<Transaction> findTransactionsByAddress(String address);
	@Query(value = "SELECT * FROM transaction WHERE id >= ?1 AND id <= ?2 AND (from_addr = ?3 OR to_addr = ?3) ORDER BY transaction_timestamp DESC", nativeQuery = true)
	List<Transaction> getTransactionsForAddressBetween(Long startId, Long endId, String address);
	@Query(value = "select from_addr, avg(nrg_price), avg(nrg_consumed), count(*) as count from transaction where id between ?1 and ?2 group by from_addr order by count desc limit 250;", nativeQuery = true)
	List<Object> getFromTransactionByAccountByIdBetween(Long startId, Long endId);
	@Query(value = "select to_addr, avg(nrg_price), avg(nrg_consumed), count(*) as count from transaction where id between ?1 and ?2 group by to_addr order by count desc limit 250;", nativeQuery = true)
	List<Object> getToTransactionByAccountByIdBetween(Long startId, Long endId);
}
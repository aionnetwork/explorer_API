package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionJpaRepository extends PagingAndSortingRepository<Transaction,Long> , JpaSpecificationExecutor<Transaction> {
	@Query(value = "SELECT to_addr, AVG(nrg_price), AVG(nrg_consumed), COUNT(*) as count FROM transaction WHERE  block_timestamp BETWEEN ?1 AND ?2 AND ((YEAR =?3 AND MONTH =?4) OR (YEAR=?5 AND MONTH=?6))GROUP BY to_addr ORDER BY count DESC LIMIT 250", nativeQuery = true)
	List<Object> findAvgsAndCountForToAddressByTimestampRange(long start, long end, int yearStart, int monthStart, int yearEnd, int monthEnd);
	@Query(value = "SELECT from_addr, AVG(nrg_price), AVG(nrg_consumed), COUNT(*) as count FROM transaction WHERE block_timestamp BETWEEN ?1 AND ?2 AND ((YEAR =?3 AND MONTH =?4) OR (YEAR=?5 AND MONTH=?6))  GROUP BY from_addr ORDER BY count DESC LIMIT 250", nativeQuery = true)
	List<Object> findAvgsAndCountForFromAddressByTimestampRange(long start, long end, int yearStart, int monthStart, int yearEnd, int monthEnd);

	@Query(value = "SELECT * FROM transaction WHERE from_addr = ?1 OR to_addr = ?1 ORDER BY block_number DESC", nativeQuery = true)
	Page<Transaction> findTransactionsByAddress(String address, Pageable pageable);

	Page<Transaction> findByDayAndMonthAndYear(int day, int month, int year, Pageable pageable);

	Transaction findByTransactionHash(String transactionHash);

	Page<Transaction> findByBlockNumber(long blockNumber, Pageable pageable);

}
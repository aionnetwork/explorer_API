package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockJpaRepository extends PagingAndSortingRepository<Block, Long>, JpaSpecificationExecutor<Block> {
	@Query(value = "SELECT miner_address, AVG(num_transactions), COUNT(*) as countBlk FROM block WHERE block_timestamp BETWEEN ?1 AND ?2 GROUP BY miner_address ORDER BY countBlk DESC LIMIT 250", nativeQuery = true)
	List<Object> findAvgAndCountForAddressBetweenTimestamp(long start, long end);


//	@Query(value = "SELECT miner_address, AVG(num_transactions), COUNT(*) as count FROM block WHERE block_timestamp BETWEEN ?1 AND ?2 GROUP BY miner_address ORDER BY count DESC LIMIT 250", nativeQuery = true)
	long countByBlockTimestampBetween(long start, long end);

	Page<Block> findByDayAndMonthAndYear(int day, int month, int year, Pageable pageable);

	Block findByBlockNumber(Long blockNumber);
    Block findByBlockHash(String blockHash);
}
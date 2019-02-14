package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockJpaRepository extends PagingAndSortingRepository<Block, Long> {
	@Query(value = "select block_number,block_timestamp from block where block_number >= ?1 and block_number <= ?2 order by block_number desc", nativeQuery = true)
	List<Object> getTimestampBetween(long start,long end);
	@Query(value = "select sum(num_transactions),max(num_transactions) from block where block_number >= ?1 and block_number <= ?2", nativeQuery = true)
	List<Object> getSumAndMaxTransactionsInBlockRange(long start,long end);
	@Query(value = "select miner_address, avg(num_transactions), count(*) as count from block where block_number between ?1 and ?2 group by miner_address order by count desc limit 250;", nativeQuery = true)
	List<Object> getBlocksMinedByAccountByBlockNumberBetween(Long startBlockNumber, Long endBlockNumber);
	@Query(value = "SELECT * FROM block WHERE miner_address = ?1 ORDER BY block_number DESC", nativeQuery = true)
	Page<Block> findByMinerAddress(String minerAddress, Pageable pageable);
	Page<Block> findByBlockNumberBetween(Long startBlockNumber,Long endBlockNumber,Pageable pageable);
	Block findByBlockNumber(Long blockNumber);
	Block findByBlockHash(String blockHash);
}
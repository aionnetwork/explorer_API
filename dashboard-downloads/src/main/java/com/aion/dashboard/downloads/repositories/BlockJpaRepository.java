package com.aion.dashboard.downloads.repositories;

import com.aion.dashboard.downloads.entities.Block;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlockJpaRepository extends PagingAndSortingRepository<Block, Long> {
	List<Block> findAllByMonthBetweenAndYearBetweenAndBlockTimestampBetweenAndMinerAddressOrderByBlockNumberDesc
			(int monthStart, int monthEnd, int yearStart, int yearEnd, long timestampStart, long timestampEnd, String minerAddress);
	Block findByBlockNumber(Long blockNumber);

	List<Block> findAll(Specification<Block> checkTime);
}
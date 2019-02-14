package com.aion.dashboard.repository;

import com.aion.dashboard.entities.BlockMap;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockMapJpaRepository extends PagingAndSortingRepository<BlockMap, String> {
}

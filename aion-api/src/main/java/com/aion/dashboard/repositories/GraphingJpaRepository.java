package com.aion.dashboard.repositories;

import java.util.List;

import com.aion.dashboard.entities.Graphing;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface GraphingJpaRepository extends PagingAndSortingRepository<Graphing, Long> {
    @Query(value = "SELECT * FROM graphing WHERE timestamp >= ?1 AND timestamp <= ?2 AND graph_type = ?3 ORDER BY timestamp ASC", nativeQuery = true)
    List<Graphing> getGraphInfo(Long start, Long end, String graphType);
    @Query(value = "SELECT SUM(value) as value, MAX(id) as id, MAX(day) as day, MAX(year) as year, MAX(month) as month, MAX(block_number) as block_number, MAX(timestamp) as timestamp, graph_type, detail FROM graphing WHERE timestamp >= ?1 AND timestamp <= ?2 AND graph_type = 'Top Miner' GROUP BY detail ORDER BY value DESC LIMIT 25", nativeQuery = true)
    List<Object> getTopMiner(Long start, Long end);
}

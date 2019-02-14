package com.aion.dashboard.repository;

import com.aion.dashboard.entities.Graphing;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraphingJpaRepository extends PagingAndSortingRepository<Graphing, Long> {
    @Query(value = "SELECT timestamp FROM graphing ORDER BY timestamp DESC LIMIT 1", nativeQuery = true)
    Long getLatestTimestamp();
    @Query(value = "SELECT * FROM graphing WHERE timestamp >= ?1 AND timestamp <= ?2 AND graph_type = ?3 ORDER BY timestamp ASC", nativeQuery = true)
    List<Graphing> getGraphInfo(Long start, Long end, String graph_type);
    @Query(value = "SELECT SUM(value) as value, MAX(id) as id, MAX(date) as date, MAX(year) as year, MAX(month) as month, MAX(block_number) as block_number, MAX(timestamp) as timestamp, graph_type, detail FROM graphing WHERE timestamp >= ?1 AND timestamp <= ?2 AND graph_type = 'Top Miner' GROUP BY detail ORDER BY value DESC LIMIT 25", nativeQuery = true)
    List<Object> getTopMiner(Long start, Long end);
}

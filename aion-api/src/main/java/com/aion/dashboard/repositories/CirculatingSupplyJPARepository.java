package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.CirculatingSupply;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CirculatingSupplyJPARepository extends
    JpaRepository< CirculatingSupply, BigDecimal> {
    Page<CirculatingSupply> findByOrderByTimestampDesc( Pageable pageable);

}

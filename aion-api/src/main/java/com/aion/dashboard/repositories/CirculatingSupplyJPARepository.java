package com.aion.dashboard.repositories;

import com.aion.dashboard.entities.CirculatingSupply;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CirculatingSupplyJPARepository extends
    JpaRepository< CirculatingSupply, Timestamp> {
}

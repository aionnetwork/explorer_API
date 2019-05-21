package com.aion.dashboard.services.mysql;

import com.aion.dashboard.entities.Metrics;
import com.aion.dashboard.repositories.MetricsJpaRepository;
import com.aion.dashboard.services.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MetricsImpMysql implements MetricsService {
    @Autowired
    private MetricsJpaRepository metricsJpaRepository;

    @Override
    public Optional<Metrics> findById(int id) {
        return metricsJpaRepository.findById(id);
    }
}

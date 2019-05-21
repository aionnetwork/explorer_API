package com.aion.dashboard.services;

import com.aion.dashboard.entities.Metrics;

import java.util.Optional;

public interface MetricsService {
    Optional<Metrics> findById(int id);
}

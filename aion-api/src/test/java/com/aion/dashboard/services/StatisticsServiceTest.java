package com.aion.dashboard.services;

import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.controllers.mapper.MetricsMapper;
import com.aion.dashboard.datatransferobject.MetricsDTO;
import com.aion.dashboard.entities.Metrics;
import com.aion.dashboard.view.Result;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
class StatisticsServiceTest {

    @Autowired
    StatisticsService service;
    @Autowired
    BlockService blockService;
    @Test
    void getSbMetrics() {
        final Metrics sbMetrics = service.getSbMetrics();
        final Result<MetricsDTO> metricsDTOResult = MetricsMapper.makeMetricsDTO(sbMetrics, blockService.blockNumber());

        assertNotNull(metricsDTOResult);
        assertFalse(metricsDTOResult.getContent().isEmpty());
    }

    @Test
    void getRtMetrics() {
        final Metrics rtMetrics = service.getRtMetrics();
        final Result<MetricsDTO> metricsDTOResult = MetricsMapper.makeMetricsDTO(rtMetrics, blockService.blockNumber());

        assertNotNull(metricsDTOResult);
        assertFalse(metricsDTOResult.getContent().isEmpty());
    }
}
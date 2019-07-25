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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @org.junit.Test
    public void createHealthDTO(){
        var res0 = StatisticsService.createHealthFrom(1,1, System.currentTimeMillis()/1000);
        var res1 = StatisticsService.createHealthFrom(1,6, System.currentTimeMillis()/1000);
        var res2 = StatisticsService.createHealthFrom(1,1, System.currentTimeMillis()/1000 - 10*60);

        assertEquals("OK", res0.getStatus());
        assertEquals("SYNCING", res1.getStatus());
        assertEquals("STALLED", res2.getStatus());



        assertEquals(res0.getDbBlockHead(), 1);
        assertEquals(res0.getBlockchainHead(), 1);
        assertEquals(res1.getDbBlockHead(), 1);
        assertEquals(res1.getBlockchainHead(), 6);
        assertEquals(res2.getDbBlockHead(), 1);
        assertEquals(res2.getBlockchainHead(),1);

        var currStatus = assertDoesNotThrow(()-> service.health());
        assertNotNull(currStatus);
        assertTrue(List.of("OK", "STALLED", "SYNCING").contains(currStatus.getStatus()));
    }
}
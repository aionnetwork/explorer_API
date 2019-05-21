package com.aion.dashboard.services;

import com.aion.dashboard.AionDashboardApiApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
class BlockServiceTest {

    @Autowired
    BlockService blockService;

    @Test
    void getBlockList() throws Exception {
        String block=blockService.getBlockList(1,30,0,10);

    }

    @Test
    void getBlocksMinedByAddress()throws Exception {
    }

    @Test
    void getBlockAndTransactionDetailsFromBlockNumberOrBlockHash()throws Exception {
    }

    @Test
    void getBlockNumber() throws Exception{
    }

    @Test
    void findByDayAndMonthAndYear()throws Exception {
    }

    @Test
    void findByBlockNumber() throws Exception{
    }

    @Test
    void findByBlockHash() throws Exception{
    }

    @Test
    void countByBlockTimestampBetween()throws Exception {
    }

    @Test
    void findAvgAndCountForAddressBetweenTimestamp()throws Exception {
    }
}
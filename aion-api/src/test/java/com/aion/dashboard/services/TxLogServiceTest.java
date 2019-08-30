package com.aion.dashboard.services;


import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.controllers.mapper.TxLogMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
public class TxLogServiceTest {

    @Autowired
    private TxLogService service;
    private String contractAddr;
    private String transactionHash;
    private long blockNumber;
    private long timeStart;
    private long timeEnd;
    private long blockStart;
    private long blockEnd;



    public TxLogServiceTest( ){
        contractAddr = "a03d2f7fc9f53b8513691b3d5309c70474da540ade4dc522afd5eaa85768d23c";
        transactionHash = "0001a2daf13c183e64fbb2f899987875dd41546b465b2a5a9317b210e754097c";
        blockNumber = 2928506;
        timeStart = 1555361900;
        timeEnd = 1561250176;
        blockStart = 2928506;
        blockEnd = 2928507;
    }

    @Test
    public void findLogsForTransaction() {
        var res = service.findLogsForTransaction(transactionHash);
        assertNotNull(res);
        assertFalse(res.isEmpty());
    }

    @Test
    public void findLogsForBlock() {

        var res = service.findLogsForBlock(blockNumber, 0, 25);
        assertNotNull(res);
        assertTrue(res.hasContent());
    }

    @Test
    public void findLogsForContract() {
        var res = service.findLogsForContract(contractAddr, 0, 25);
        assertNotNull(res);
        assertTrue(res.hasContent());
    }

    @Test
    public void findLogsForContractAndInTimeRange() {
        var res = service.findLogsForContractAndInTimeRange(contractAddr, timeStart, timeEnd, 0, 25);
        assertNotNull(res);
        assertTrue(res.hasContent());
    }

    @Test
    public void findLogsForContractAndInBlockRange() {
        var res = service.findLogsForContractAndInBlockRange(contractAddr, blockStart, blockEnd, 0, 25);
        assertNotNull(res);
        assertTrue(res.hasContent());
    }

    @Test
    public void findLogsInTimeRange() {
        var res = service.findLogsInTimeRange(timeStart, timeEnd, 0, 25);
        assertNotNull(res);
        assertTrue(res.hasContent());
    }

    @Test
    public void findLogsForBlockRange() {

        var res = service.findLogsForBlockRange(blockStart, blockEnd, 0, 25);
        assertNotNull(res);
        assertTrue(res.hasContent());
    }


    @Test
    public void testDTO(){
        var res = service.findLogsForTransaction(transactionHash);
        var result = TxLogMapper.getInstance().makeResult(res.stream().findFirst().orElseThrow());
        assertFalse(result.getContent().isEmpty());

    }

}
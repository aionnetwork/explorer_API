package com.aion.dashboard.services;


import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.controllers.mapper.BlockMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
public class BlockServiceTest {

    @Autowired
    BlockService blockService;

    @Test
    public void findByBlockNumber() throws Exception {
        assertTrue(blockService.findByBlockNumber(10000l).getBlockNumber()==10000l);

    }

    @Test
    public void getHeightBlock()throws Exception {
        assertNotNull(blockService.getHeightBlock());
        assertTrue(blockService.getHeightBlock().getBlockNumber()>0);
    }


    @Test
    public void given_blockDOValidateWhitMapper()throws Exception {
        var b=blockService.getHeightBlock();
        assertNotNull(b);
        assertTrue(b.getBlockNumber()>0);



        assertNotNull(BlockMapper.makeBlockDTO(b).getBlockReward());
        assertTrue(BlockMapper.makeBlockDTO(b).getBlockReward()==b.getBlockReward());
        assertTrue(BlockMapper.makeBlockDTO(b).getBlockHash().equals(b.getBlockHash()));
        assertTrue(BlockMapper.makeBlockDTO(b).getBlockNumber()==b.getBlockNumber());
        assertTrue(BlockMapper.makeBlockDTO(b).getBlockTime()==b.getBlockTime());
        assertTrue(BlockMapper.makeBlockDTO(b).getBlockTimestamp()==b.getBlockTimestamp());
        assertTrue(BlockMapper.makeBlockDTO(b).getBloom().equals(b.getBloom()));
        assertTrue(BlockMapper.makeBlockDTO(b).getDay()==b.getDay());
        assertTrue(BlockMapper.makeBlockDTO(b).getDifficulty()==b.getDifficulty());
        assertTrue(BlockMapper.makeBlockDTO(b).getExtraData().equals(b.getExtraData()));
        assertTrue(BlockMapper.makeBlockDTO(b).getMinerAddress().equals(b.getMinerAddress()));
        assertTrue(BlockMapper.makeBlockDTO(b).getMonth()==b.getMonth());
        assertTrue(BlockMapper.makeBlockDTO(b).getNrgConsumed()==b.getNrgConsumed());
        assertTrue(BlockMapper.makeBlockDTO(b).getNonce().equals(b.getNonce()));
        assertTrue(BlockMapper.makeBlockDTO(b).getNrgLimit()==b.getNrgLimit());
        assertTrue(BlockMapper.makeBlockDTO(b).getNrgReward()==b.getNrgReward());
        assertTrue(BlockMapper.makeBlockDTO(b).getNumTransactions()==b.getNumTransactions());
        assertTrue(BlockMapper.makeBlockDTO(b).getParentHash().equals(b.getParentHash()));
        assertTrue(BlockMapper.makeBlockDTO(b).getReceiptTxRoot().equals(b.getReceiptTxRoot()));
        assertTrue(BlockMapper.makeBlockDTO(b).getSize()==b.getSize());
        assertTrue(BlockMapper.makeBlockDTO(b).getSolution().equals(b.getSolution()));


    }
}
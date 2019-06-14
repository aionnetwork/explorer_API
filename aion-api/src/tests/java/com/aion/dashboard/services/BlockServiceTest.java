package com.aion.dashboard.services;


import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.controllers.mapper.TransactionMapper;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.utility.Utility;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.*;

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
}
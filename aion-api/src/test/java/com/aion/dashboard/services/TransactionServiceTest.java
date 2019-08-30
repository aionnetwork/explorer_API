package com.aion.dashboard.services;

import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.controllers.mapper.TransactionMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.jupiter.api.Assertions.assertTrue;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;
    private TransactionMapper mapper = TransactionMapper.getInstance();

    @Test
    public void whenTransactionsByBlockNumberIsCompareWithThMapper() {

        var t=transactionService.findByBlockNumber(3353627,1,10);
        assertTrue( t.getTotalElements()==34);

        assertTrue(mapper.makeResult(t).getPage().getTotalElements()==t.getTotalElements());
        assertTrue(mapper.makeResult(t).getPage().getTotalPages()==t.getTotalPages());
        assertTrue(mapper.makeResult(t).getPage().getPage()==t.getNumber());
        assertTrue(mapper.makeResult(t).getPage().getSize()==t.getSize());
    }

    @Test
    public void whenTransactionsByBlockHashIsCompareWithThMapper() {

        var t=transactionService.findByBlockHash("5ee584420b48fa5432e412a630bf179c0fa6755f92a2377b6f3e9e22d30f2732",1,10);
        assertTrue( t.getTotalElements()==34);
        assertTrue(mapper.makeResult(t).getPage().getTotalElements()==t.getTotalElements());
        assertTrue(mapper.makeResult(t).getPage().getTotalPages()==t.getTotalPages());
        assertTrue(mapper.makeResult(t).getPage().getPage()==t.getNumber());
        assertTrue(mapper.makeResult(t).getPage().getSize()==t.getSize());
        assertTrue(mapper.makeResult(t).getContent().get(0).getBlockNumber()==3353627);



    }
    //block 3353627 has 34 transactions
    //0x5ee584420b48fa5432e412a630bf179c0fa6755f92a2377b6f3e9e22d30f2732
    @Test
    public void whenTransactionsByBlockHashHasDateRange() {

        var t=transactionService.findByBlockHash("5ee584420b48fa5432e412a630bf179c0fa6755f92a2377b6f3e9e22d30f2732",1,10);
        assertTrue( t.getTotalElements()==34);
        assertTrue(mapper.makeResult(t).getPage().getTotalElements()==t.getTotalElements());
        assertTrue(mapper.makeResult(t).getPage().getTotalPages()==t.getTotalPages());
        assertTrue(mapper.makeResult(t).getPage().getPage()==t.getNumber());
        assertTrue(mapper.makeResult(t).getPage().getSize()==t.getSize());
        assertTrue(mapper.makeResult(t).getContent().get(0).getBlockNumber()==3353627);



    }

    @Test
    public void whenTransactionsByTime() {

        var t=transactionService.findByTime(1,10,
                1559847110,1559847110);
        assertTrue( t.getTotalElements()==34);
        assertTrue(mapper.makeResult(t).getPage().getTotalElements()==t.getTotalElements());
        assertTrue(mapper.makeResult(t).getPage().getTotalPages()==t.getTotalPages());
        assertTrue(mapper.makeResult(t).getPage().getPage()==t.getNumber());
        assertTrue(mapper.makeResult(t).getPage().getSize()==t.getSize());
        assertTrue(mapper.makeResult(t).getContent().get(0).getBlockNumber()==3353627);



    }
}
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
public class TransactionServiceTest {

    @Autowired
    TransactionService transactionService;

    @Test
    public void whenTransactionsByBlockNumberIsCompareWithThMapper() {

        var t=transactionService.findByBlockNumber(1000000,1,10);
        assertTrue( t.getTotalElements()==9);
        assertTrue(TransactionMapper.makeTransactionDTOList(t).getTotalElements()==t.getTotalElements());
        assertTrue(TransactionMapper.makeTransactionDTOList(t).getTotalPages()==t.getTotalPages());
        assertTrue(TransactionMapper.makeTransactionDTOList(t).getNumber()==t.getNumber());
        assertTrue(TransactionMapper.makeTransactionDTOList(t).getSize()==t.getSize());


    }
}
package com.aion.dashboard.services;


import com.aion.dashboard.AionDashboardApiApplication;
import com.aion.dashboard.entities.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AionDashboardApiApplication.class)
public class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    public void whenSearchingForAddress(){
        Account account= accountService.findByAddress("a0eeaeabdbc92953b072afbd21f3e3fd8a4a4f5e6a6e22200db746ab75e9a99a");
        assertTrue(Optional.ofNullable(account).isPresent());
    }

    @Test
    public void whichTheTopsAccounts(){
        Page<Account> account= accountService.getRichList(PageRequest.of(0, 25, new Sort(Sort.Direction.DESC, "balance")));
        assertTrue(account.getTotalPages()>0);
    }
}

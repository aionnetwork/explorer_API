package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.AccountDTO;
import com.aion.dashboard.entities.Account;

public class AccountMapper extends Mapper<Account, AccountDTO>{
    private static final AccountMapper mapper = new AccountMapper();

    public static AccountMapper getMapper() {
        return mapper;
    }

    @Override
    AccountDTO makeDTO(Account account) {
        return new AccountDTO(account.getAddress(), account.getNonce(), account.getBalance(), account.isContract());
    }
}

package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.AccountDTO;
import com.aion.dashboard.entities.Account;

public class AccountMapper extends Mapper<Account, AccountDTO> {
    private static final AccountMapper mapper = new AccountMapper();

    public static AccountMapper getInstance() {
        return mapper;
    }
    private AccountMapper(){}
    @Override
    public AccountDTO makeDTO(Account account) {
        return new AccountDTO.AccountDTOBuilder()
                .setAddress(account.getAddress())
                .setContract(account.isContract())
                .setNonce(account.getNonce())
                .setBalance(account.getBalance())
                .createAccountDTO();
    }
}

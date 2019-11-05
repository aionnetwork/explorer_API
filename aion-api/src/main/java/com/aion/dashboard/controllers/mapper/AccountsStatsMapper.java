package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.AccountStatsDTO;
import com.aion.dashboard.entities.AccountStats;

public class AccountsStatsMapper extends Mapper<AccountStats, AccountStatsDTO> {

    private static final AccountsStatsMapper mapper = new AccountsStatsMapper();

    public static AccountsStatsMapper getMapper() {
        return mapper;
    }

    @Override
    AccountStatsDTO makeDTO(AccountStats accountStats) {
        return new AccountStatsDTO(
                accountStats.getBlockNumber(),
                accountStats.getAddress(),
                accountStats.getAionIn(),
                accountStats.getAionOut(),
                accountStats.getBlockTimestamp());
    }
}

package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.ValidatorStatsDTO;
import com.aion.dashboard.entities.ValidatorStats;

public class ValidatorStatsMapper extends Mapper<ValidatorStats, ValidatorStatsDTO>{

    private static final ValidatorStatsMapper mapper = new ValidatorStatsMapper();

    public static ValidatorStatsMapper getInstance() {
        return mapper;
    }

    @Override
    ValidatorStatsDTO makeDTO(ValidatorStats validatorStats) {
        return new ValidatorStatsDTO(
                validatorStats.getBlockNumber(),
                validatorStats.getMinerAddress(),
                validatorStats.getSealType(),
                validatorStats.getBlockCount(),
                validatorStats.getBlockTimestamp(),
                validatorStats.getPercentageOfBlocksValidated(),
                validatorStats.getAverageTransactionsPerBlock());
    }
}

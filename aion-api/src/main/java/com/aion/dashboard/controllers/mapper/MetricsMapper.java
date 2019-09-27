package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.MetricsDTO;
import com.aion.dashboard.entities.Metrics;
import com.aion.dashboard.view.Result;

public class MetricsMapper {
    public static Result<MetricsDTO> makeMetricsDTO(Metrics metrics, long blockNumber){
        return Result.from(makeDTO(metrics, blockNumber));
    }

    static MetricsDTO makeDTO(Metrics metrics, long blockNumber) {
        return new MetricsDTO.MetricsDTOBuilder()
                .setAveragedBlockTime(metrics.getAveragedBlockTime())
                .setLastBlockReward(metrics.getLastBlockReward())
                .setAveragedHashPower(metrics.getAveragedHashPower())
                .setAverageDifficulty(metrics.getAverageDifficulty())
                .setAverageNrgConsumed(metrics.getAverageNrgConsumed())
                .setAverageNrgLimit(metrics.getAverageNrgLimit())
                .setCurrentBlockchainHead(blockNumber)
                .setEndBlock(metrics.getEndBlock())
                .setStartBlock(metrics.getStartBlock())
                .setEndTimestamp(metrics.getEndTimestamp())
                .setId(metrics.getId())
                .setPeakTransactionsPerBlock(metrics.getPeakTransactionsPerBlock())
                .setStartTimestamp(metrics.getStartTimestamp())
                .setTotalTransaction(metrics.getTotalTransaction())
                .setTransactionsPerSecond(metrics.getTransactionsPerSecond())
                .setAveragePosIssuance(metrics.getAvgPosIssuance())
                .setPosBlockDifficulty(metrics.getPosAvgDifficulty())
                .setPercentageOfNetworkStaking(metrics.getPercentageOfNetworkStaking())
                .setPosBlockTime(metrics.getPosAvgBlockTime())
                .setPowBlockDifficulty(metrics.getPowAvgDifficulty())
                .setPowBlockTime(metrics.getPowAvgBlockTime())
                .setTotalStake(metrics.getTotalStake())
                .buildMetricsDTO();
    }
}

package com.aion.dashboard.datatransferobject;

import java.util.List;

public class ViewDTO {
    private final List<BlockDTO> blocks;
    private final List<TransactionDTO> transactions;
    private final MetricsDTO metrics;

    public ViewDTO(List<BlockDTO> blocks,
        List<TransactionDTO> transactions, MetricsDTO metrics) {
        this.blocks = blocks;
        this.transactions = transactions;
        this.metrics = metrics;
    }

    public List<BlockDTO> getBlocks() {
        return blocks;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public MetricsDTO getMetrics() {
        return metrics;
    }
}

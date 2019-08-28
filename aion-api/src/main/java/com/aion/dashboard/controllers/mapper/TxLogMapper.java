package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TxLogDTO;
import com.aion.dashboard.entities.TxLog;

import java.util.Arrays;

public class TxLogMapper extends Mapper<TxLog,TxLogDTO> {

    private static final TxLogMapper mapper = new TxLogMapper();

    public static TxLogMapper getInstance() {
        return mapper;
    }

    private TxLogMapper(){}
    @Override
    protected TxLogDTO makeDTO(TxLog txLog) {
        return makeTxLogDTO(txLog);
    }
    private static TxLogDTO makeTxLogDTO(TxLog log){
        String topics = log.getTopics();
        var topicsList = Arrays.asList(topics.replaceAll("([|]|\")", "").split(","));

        return new TxLogDTO.TxLogDTOBuilder().setBlockNumber(log.getBlockNumber())
                .setBlockTimestamp(log.getBlockTimestamp())
                .setContractAddr(log.getContractAddr())
                .setContractType(log.getContractType())
                .setData(log.getData())
                .setFromAddr(log.getFromAddr())
                .setToAddr(log.getToAddr())
                .setTopics(topicsList)
                .setLogIndex(log.getLogIndex())
                .setTransactionHash(log.getTransactionHash()).build();

    }
}

package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TxLogDTO;
import com.aion.dashboard.entities.TxLog;
import com.aion.dashboard.view.Result;
import org.springframework.data.domain.Page;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TxLogMapper {


    public static List<TxLogDTO> makeTxLogDTOList(List<TxLog> logs) {
        return  logs.stream().map(TxLogMapper::makeTxLogDTO).collect(Collectors.toList());
    }

    public static Result<TxLogDTO> makeResult(Page<TxLog> logPage){
        return Result.from(makeTxLogDTOList(logPage.getContent()),logPage);
    }

    public static Result<TxLogDTO> makeResult(List<TxLog> logList){
        return Result.from(makeTxLogDTOList(logList));
    }

    public static Result<TxLogDTO> makeResult(TxLog logList){
        return Result.from(makeTxLogDTO(logList));
    }

    public static TxLogDTO makeTxLogDTO(TxLog log){
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

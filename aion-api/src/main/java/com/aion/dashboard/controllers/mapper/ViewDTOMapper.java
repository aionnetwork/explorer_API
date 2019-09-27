package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.ViewDTO;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.Metrics;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.view.Result;
import org.springframework.data.domain.Page;

public class ViewDTOMapper {

    private static BlockMapper blockMapper = BlockMapper.getInstance();
    private static TransactionMapper transactionMapper = TransactionMapper.getInstance();
    public static Result<ViewDTO> makeDTO(Metrics metrics,long blockNumber, Page<Transaction> transactionPage, Page<Block> blockPage){
        return Result.from(new ViewDTO(blockMapper.makeDTOList(blockPage.getContent()), transactionMapper.makeDTOList(transactionPage.getContent()), MetricsMapper.makeDTO(metrics, blockNumber)));
    }
    private ViewDTOMapper(){
        throw new UnsupportedOperationException();
    }
}

package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.BlockDTO;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.view.Result;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BlockMapper {


    /**
     * @param block Data Transfer Object of block
     * @return A Block object
     */
    public static Block makeBlock(BlockDTO block)
    {
        return new Block(block.getBlockNumber(), block.getNrgConsumed(), block.getNrgLimit(), block.getSize(),
                block.getBlockTimestamp(), block.getNumTransactions(), block.getBlockTime(), block.getNrgReward(),
                block.getDifficulty(), block.getTotalDifficulty(), block.getYear(), block.getMonth(), block.getDay(),
                block.getBlockHash(), block.getMinerAddress(), block.getParentHash(), block.getReceiptTxRoot(),
                block.getStateRoot(), block.getTxTrieRoot(), block.getExtraData(), block.getNonce(), block.getBloom(),
                block.getSolution(), block.getTransactionHash(), block.getTransactionHashes(),block.getBlockReward());

    }


    /**
     * @param block Block object
     * @return A  Data Transfer Object of block
     */
    public static BlockDTO makeBlockDTO(Block block)
    {
        BlockDTO.BlockDTOBuilder blockDTOBuilder = BlockDTO.newBuilder().setBlockHash(block.getBlockHash()).
                setBlockNumber(block.getBlockNumber()).setBlockReward(block.getBlockReward()).setBlockTime(block.getBlockTime()).
                setBlockTimestamp(block.getBlockTimestamp()).setBloom(block.getBloom()).setDay(block.getDay()).setDifficulty(block.getDifficulty()).
                setExtraData(block.getExtraData()).setMinerAddress(block.getMinerAddress()).setMonth(block.getMonth()).setNonce(block.getNonce()).
                setNrgConsumed(block.getNrgConsumed()).setNrgLimit(block.getNrgLimit()).setNrgReward(block.getNrgReward()).
                setNumTransactions(block.getNumTransactions()).setParentHash(block.getParentHash()).setReceiptTxRoot(block.getReceiptTxRoot()).
                setSize(block.getSize()).setSolution(block.getSolution()).setStateRoot(block.getStateRoot()).setTotalDifficulty(block.getTotalDifficulty()).
                setTransactionHash(block.getTransactionHash()).setTransactionHashes(block.getTransactionHashes()).setTxTrieRoot(block.getTxTrieRoot()).
                setYear(block.getYear());


        return blockDTOBuilder.createBlockDTO();
    }


    /**
     * @param block Block in a Page object
     * @return A  List of Data Transfer Object of block
     */
    public static Result<BlockDTO> makeBlockDTOList(Page<Block> block)
    {

        return Result.from(block.getContent().stream()
                .map(BlockMapper::makeBlockDTO)
                .collect(Collectors.toList()), block);
    }
}

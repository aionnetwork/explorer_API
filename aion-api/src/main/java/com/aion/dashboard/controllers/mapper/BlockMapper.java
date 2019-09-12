package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.BlockDTO;
import com.aion.dashboard.entities.Block;

public class BlockMapper extends Mapper<Block, BlockDTO> {

    private static final BlockMapper mapper = new BlockMapper();

    public static BlockMapper getInstance() {
        return mapper;
    }

    private BlockMapper(){}
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
                setYear(block.getYear()).setPublicKey(block.getPublicKey()).setSealType(block.getSealType()).setSeed(block.getSeed()).setSignature(block.getSignature());


        return blockDTOBuilder.createBlockDTO();
    }

    @Override
    protected BlockDTO makeDTO(Block block) {
        return makeBlockDTO(block);
    }
}

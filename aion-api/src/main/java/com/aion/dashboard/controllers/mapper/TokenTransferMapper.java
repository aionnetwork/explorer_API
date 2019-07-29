package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TokenTransfersDTO;
import com.aion.dashboard.entities.TokenTransfers;

public class TokenTransferMapper extends Mapper<TokenTransfers, TokenTransfersDTO> {

    private static final TokenTransferMapper mapper = new TokenTransferMapper();

    public static TokenTransferMapper getInstance() {
        return mapper;
    }
    private TokenTransferMapper(){}
    @Override
    public TokenTransfersDTO makeDTO(TokenTransfers tokenTransfers) {
        return new TokenTransfersDTO.TokenTransfersDTOBuilder()
                .setBlockNumber(tokenTransfers.getBlockNumber())
                .setContractAddr(tokenTransfers.getContractAddr())
                .setFromAddr(tokenTransfers.getFromAddr())
                .setGranularity(tokenTransfers.getGranularity())
                .setOperatorAddr(tokenTransfers.getOperatorAddr())
                .setRawValue(tokenTransfers.getRawValue())
                .setTknValue(tokenTransfers.getTknValue())
                .setToAddr(tokenTransfers.getToAddr())
                .setTokenDecimal(tokenTransfers.getTokenDecimal())
                .setTransactionHash(tokenTransfers.getTransactionHash())
                .setTransferTimestamp(tokenTransfers.getTransferTimestamp())
                .createTokenTransfersDTO();
    }
}

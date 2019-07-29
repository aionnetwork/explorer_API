package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TokenHoldersDTO;
import com.aion.dashboard.entities.TokenHolders;

public class TokenHoldersMapper extends Mapper<TokenHolders, TokenHoldersDTO> {

    private static final TokenHoldersMapper mapper = new TokenHoldersMapper();

    public static TokenHoldersMapper getInstance() {
        return mapper;
    }
    private TokenHoldersMapper(){}
    @Override
    public TokenHoldersDTO makeDTO(TokenHolders tokenHolders) {
        return new TokenHoldersDTO.TokenHoldersDTOBuilder()
                .setBlockNumber(tokenHolders.getBlockNumber())
                .setContractAddr(tokenHolders.getContractAddr())
                .setGranularity(tokenHolders.getGranularity())
                .setHolderAddr(tokenHolders.getHolderAddr())
                .setRawBalance(tokenHolders.getRawBalance())
                .setTknBalance(tokenHolders.getTknBalance())
                .setTokenDecimal(tokenHolders.getTokenDecimal())
                .createTokenHoldersDTO();
    }
}

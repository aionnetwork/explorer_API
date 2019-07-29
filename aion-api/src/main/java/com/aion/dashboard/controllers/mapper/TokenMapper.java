package com.aion.dashboard.controllers.mapper;

import com.aion.dashboard.datatransferobject.TokenDTO;
import com.aion.dashboard.entities.Token;

public class TokenMapper extends Mapper<Token, TokenDTO> {

    private static final TokenMapper mapper = new TokenMapper();

    public static TokenMapper getInstance() {
        return mapper;
    }

    private TokenMapper(){}
    @Override
    public TokenDTO makeDTO(Token token) {
        return new TokenDTO.TokenDTOBuilder()
                .setContractAddr(token.getContractAddr())
                .setCreationTimestamp(token.getCreationTimestamp())
                .setCreatorAddress(token.getCreatorAddress())
                .setGranularity(token.getGranularity())
                .setLiquidSupply(token.getLiquidSupply())
                .setName(token.getName())
                .setSymbol(token.getSymbol())
                .setTokenDecimal(token.getTokenDecimal())
                .setTotalSupply(token.getTotalSupply())
                .setTransactionHash(token.getTransactionHash())
                .createTokenDTO();
    }
}

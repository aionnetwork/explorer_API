package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Balance;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.repository.BalanceJpaRepository;
import com.aion.dashboard.repository.EventJpaRepository;
import com.aion.dashboard.repository.ParserStateJpaRepository;
import com.aion.dashboard.types.ParserStateType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class ThirdPartyService {
    @Autowired
    private EventJpaRepository eventJpaRepository;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private ParserStateJpaRepository parserStateJpaRepository;

    private final String MAINNET_GENESIS_ADDRESS = "a0eeaeabdbc92953b072afbd21f3e3fd8a4a4f5e6a6e22200db746ab75e9a99a";
    private final String MASTERY_GENESIS_ADDRESS = "a0b88269779d225510ca880ed742e445db0c70efb1ee3159b6d56479ae3501f9";

    private final BigDecimal TRS_VALUE = BigDecimal.valueOf(203261307);
    private final BigDecimal MINTED_BALANCE = new BigDecimal("465934586660000000000000000").scaleByPowerOfTen(-18);

    @Cacheable(CacheConfig.CIRCULATING_SUPPLY)
    public String getCirculatingSupply() {
        Optional<ParserState> blockParserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        JSONObject aionObject = new JSONObject();
        JSONArray aionArray = new JSONArray();

        if(blockParserState.isPresent()) {

            // Checks for the Genesis Address first for Mainnet and then for Mastery
            Balance account = balanceJpaRepository.findByAddress(MAINNET_GENESIS_ADDRESS);
            if(account == null) {
                account = balanceJpaRepository.findByAddress(MASTERY_GENESIS_ADDRESS);
            }

            if(account != null) {
//                BigDecimal ttlBlkRewards = RewardsCalculator.calculateTotalRewardsAtBlockNumber(blockParserState.get().getBlockNumber());
//                BigDecimal circulatingGenesis = MINTED_BALANCE.subtract(account.getBalance().add(TRS_VALUE));
//                BigDecimal circulatingSupply = ttlBlkRewards.add(circulatingGenesis);
//                aionArray.put(new JSONObject().put("circulating_Supply", circulatingSupply));
//                aionArray.put(new JSONObject().put("circulating_Minted_Supply", circulatingGenesis));
//                aionArray.put(new JSONObject().put("initial_Minted_Supply", MINTED_BALANCE));
//                aionArray.put(new JSONObject().put("TRS_Value", TRS_VALUE));
//                aionArray.put(new JSONObject().put("current_Minted_Supply", account.getBalance()));
//                aionArray.put(new JSONObject().put("latest_Block", blockParserState.get().getBlockNumber()));
//                aionArray.put(new JSONObject().put("total_Block_Rewards",ttlBlkRewards));
//                aionObject.put("content", aionArray);
            }
        }

        // If the ResultSet is Null
        if(aionArray.length() == 0) {
            aionArray = new JSONArray();
            aionObject.put("content", aionArray);
        }

//        return aionObject.toString();

        String circulatingSupply = "283,009,162";
        return circulatingSupply;
    }

    @Cacheable(CacheConfig.COUNT_OPERATIONS)
    public String countOperations(Long startTime, Long endTime) {
        JSONObject operationsObject = new JSONObject();
        JSONArray operationsArray = new JSONArray();

        Long operations = eventJpaRepository.getCountBetween(startTime, endTime);
        if (operations != null) {
            JSONObject result = new JSONObject().put("operations", operations);
            operationsArray.put(result);
            operationsObject.put("content", operationsArray);
            return operationsObject.toString();
        } else return new JSONObject().put("rel", JSONObject.NULL).toString();
    }
}

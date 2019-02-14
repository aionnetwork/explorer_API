package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.*;
import com.aion.dashboard.repository.BalanceJpaRepository;
import com.aion.dashboard.repository.ParserStateJpaRepository;
import com.aion.dashboard.repository.TokenBalanceJpaRepository;
import com.aion.dashboard.repository.TokenJpaRepository;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BalanceService {

    @Autowired
    private TokenJpaRepository tokenJpaRepository;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private ParserStateJpaRepository parserStateJpaRepository;

    @Autowired
    private TokenBalanceJpaRepository tokenBalanceJpaRepository;

    @Cacheable(CacheConfig.ACCOUNT_DETAILS)
    public String getAccountDetails(String accountAddress, String tokenAddress) throws Exception {
        try {
            if (accountAddress.startsWith("0x")) accountAddress = accountAddress.replace("0x", "");
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject balanceObject = new JSONObject();
            JSONArray balanceArray = new JSONArray();

            if (Utility.validHex(accountAddress) && parserState.isPresent()) {
                Balance balance = balanceJpaRepository.findByAddress(accountAddress);
                if (balance != null) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(balance));
                    JSONArray tokensArray =  new JSONArray(getAccountTokenList(balance.getAddress()));
                    result.put("tokens", tokensArray);

                    // Getting the LastBlockNumber from the Parser State
                    result.put("lastBlockNumber", parserState.get().getBlockNumber());

                    // Getting Token Information if Entered and Found
                    if (tokenAddress != null) {
                        if(tokenAddress.startsWith("0x")) tokenAddress = tokenAddress.replace("0x", "");
                        TokenBalance tokenBalance = tokenBalanceJpaRepository.findByContractAddrAndHolderAddr(tokenAddress, accountAddress);
                        if (tokenBalance != null) {
                            result.put("balance", tokenBalance.getTknBalance());
                            Token token = tokenJpaRepository.findByContractAddr(tokenAddress);
                            if (token != null) {
                                result.put("tokenName", token.getName());
                                result.put("tokenSymbol", token.getSymbol());
                            }
                        } else throw new Exception();
                    }

                    balanceArray.put(result);
                    balanceObject.put("content", balanceArray);
                    return balanceObject.toString();
                }
            }

            throw new Exception();
        } catch (Exception e) { throw e; }
    }

    @Cacheable(CacheConfig.ACCOUNT_RICH_LIST)
    public String getAccountRichList() {
        JSONObject balanceObject = new JSONObject();
        JSONArray balanceArray = new JSONArray();

        Page<Balance> balancePage = balanceJpaRepository.getRichList(new PageRequest(0, 25, new Sort(Sort.Direction.DESC, "balance")));
        List<Balance> balanceList = balancePage.getContent();
        if (balanceList != null && balanceList.size() > 0) {
            for (int i = 0; i < balanceList.size(); i++) {
                Balance balance = balanceList.get(i);
                JSONObject result = new JSONObject().put("address", balance.getAddress());
                result.put("balance", Utility.fromNano(balance.getBalance().toBigInteger().toString()));
                balanceArray.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put("totalElements", balancePage.getTotalElements());
            pageObject.put("totalPages", balancePage.getTotalPages());
            pageObject.put("number", 0);
            pageObject.put("size", 25);

            balanceObject.put("content", balanceArray);
            balanceObject.put("page", pageObject);

            return balanceObject.toString();
        } else return new JSONObject().put("rel", JSONObject.NULL).toString();
    }

    public String getDailyAccountStatistics() {
        try {
            DailyStatistics dailyStatistics = DailyStatistics.getInstance();

            JSONObject result = new JSONObject();
            result.put("miners", new JSONObject().put("content", dailyStatistics.getBlocksMined()));
            result.put("txnInbound", new JSONObject().put("content", dailyStatistics.getInboundTransactions()));
            result.put("txnOutbound", new JSONObject().put("content", dailyStatistics.getOutboundTransactions()));

            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject result = new JSONObject();
            result.put("message", "Error: Invalid Request");
            return result.toString();
        }
    }



    // Internal Methods
    @Cacheable(CacheConfig.ACCOUNT_TOKEN_LIST)
    private String getAccountTokenList(String holderAddress) throws Exception {
        try {
            List<TokenBalance> tokenBalanceList = tokenBalanceJpaRepository.findByHolderAddr(holderAddress);
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

            JSONArray tokenArray = new JSONArray();
            if (tokenBalanceList != null && tokenBalanceList.size() > 0) {
                for (TokenBalance tokenBalance : tokenBalanceList) tokenArray.put(new JSONObject(ow.writeValueAsString(tokenJpaRepository.findByContractAddr(tokenBalance.getContractAddr()))));
            } else tokenArray.put(new JSONObject().put("rel", JSONObject.NULL));
            return tokenArray.toString();

        } catch (Exception e) { throw e; }
    }
}

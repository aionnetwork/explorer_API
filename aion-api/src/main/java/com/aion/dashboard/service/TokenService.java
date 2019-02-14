package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenBalance;
import com.aion.dashboard.entities.Transfer;
import com.aion.dashboard.repository.*;
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

import static java.lang.Math.min;

@Component
public class TokenService {

    @Autowired
    private TokenJpaRepository tokenJpaRepository;

    @Autowired
    private TransferJpaRepository transferJpaRepository;

    @Autowired
    private ParserStateJpaRepository parserStateJpaRepository;

    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private TokenBalanceJpaRepository tokenBalanceJpaRepository;

    @Cacheable(CacheConfig.TOKEN_LIST)
    public String getTokenList(int pageNumber, int pageSize) throws Exception {
        try {
            JSONArray tokenArray = new JSONArray();
            JSONObject tokenObject = new JSONObject();
            Page<Token> tokenPage = tokenJpaRepository.findAllTokens(new PageRequest(pageNumber, min(pageSize, 5000)));

            if(tokenPage != null) {
                List<Token> tokenList = tokenPage.getContent();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                // Writing the Page/List of Tokens to a JSON Object
                if(tokenList != null && tokenList.size() > 0) {
                    for (Token token : tokenList) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(token));
                        result.put("totalHolders", getTotalTokenHolders(token.getContractAddr()));
                        result.put("totalTransfers", getTotalTokenTransfers(token.getContractAddr()));
                        tokenArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", tokenPage.getTotalElements());
                    pageObject.put("totalPages", tokenPage.getTotalPages());
                    pageObject.put("number", tokenPage.getNumber());
                    pageObject.put("size", tokenPage.getSize());

                    tokenObject.put("content", tokenArray);
                    tokenObject.put("page", pageObject);

                    return tokenObject.toString();
                }
            }

            return new JSONObject().put("rel", JSONObject.NULL).toString();
        } catch(Exception e) { throw e; }
    }

    @Cacheable(CacheConfig.TOKEN_LIST_BY_TOKEN_NAME_OR_TOKEN_SYMBOL)
    public String getTokenListByTokenNameOrTokenSymbol(String searchParam, int pageNumber, int pageSize) throws Exception {
        try {
            JSONArray tokenArray = new JSONArray();
            JSONObject tokenObject = new JSONObject();
            Page<Token> tokenPage = tokenJpaRepository.findByName(searchParam, new PageRequest(pageNumber, min(pageSize, 1000), new Sort(Sort.Direction.DESC, "creationTimestamp")));                                   // Getting the Token Details by Token Name

            if(tokenPage != null) {
                List<Token> tokenList = tokenPage.getContent();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                // Writing the Page/List of Tokens to a JSON Object
                if(tokenList != null && tokenList.size() > 0) {
                    for (Token token : tokenList) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(token));
                        result.put("totalHolders", getTotalTokenHolders(token.getContractAddr()));
                        result.put("totalTransfers", getTotalTokenTransfers(token.getContractAddr()));
                        tokenArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", tokenPage.getTotalElements());
                    pageObject.put("totalPages", tokenPage.getTotalPages());
                    pageObject.put("number", tokenPage.getNumber());
                    pageObject.put("size", tokenPage.getSize());

                    tokenObject.put("content", tokenArray);
                    tokenObject.put("page", pageObject);

                    return tokenObject.toString();
                }
            }

            tokenPage = tokenJpaRepository.findBySymbol(searchParam, new PageRequest(pageNumber, min(pageSize, 1000), new Sort(Sort.Direction.DESC, "creationTimestamp")));                                            // Getting the Token Details by Token Symbol
            if(tokenPage != null) {
                List<Token> tokenList = tokenPage.getContent();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                // Writing the Page/List of Tokens to a JSON Object
                if (tokenList != null && tokenList.size() > 0) {
                    for (Token token : tokenList) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(token));
                        result.put("totalHolders", getTotalTokenHolders(token.getContractAddr()));
                        result.put("totalTransfers", getTotalTokenTransfers(token.getContractAddr()));
                        tokenArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", tokenPage.getTotalElements());
                    pageObject.put("totalPages", tokenPage.getTotalPages());
                    pageObject.put("number", pageNumber);
                    pageObject.put("size", pageSize);

                    tokenObject.put("content", tokenArray);
                    tokenObject.put("page", pageObject);

                    return tokenObject.toString();
                }
            }

            return new JSONObject().put("rel", JSONObject.NULL).toString();
        } catch(Exception e) { throw e; }
    }

    @Cacheable(CacheConfig.TOKEN_DETAILS_TRANSFERS_AND_HOLDERS_BY_CONTRACT_ADDRESS)
    public String getTokenDetailsTransfersAndHoldersByContractAddress(String searchParam, int holderPageNumber, int holderPageSize, int transferPageNumber, int transferPageSize) throws Exception {
        try {
            if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONArray tokenArray = new JSONArray();

            if(Utility.validHex(searchParam)) {
                Token token = tokenJpaRepository.findByContractAddr(searchParam);                                      // Getting the Token Details by Contract Address
                if (token != null) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(token));
                    result.put("holders", new JSONObject(getTokenHoldersByContractAddress(token.getContractAddr(), holderPageNumber, holderPageSize)));                // Top 1,000 Holders
                    result.put("transfers", new JSONObject(getTokenTransfersByContractAddress(token.getContractAddr(), transferPageNumber, transferPageSize)));        // Last 100,000 Transfers
                    tokenArray.put(result);
                    return new JSONObject().put("content", tokenArray).toString();
                }
            }

            throw new Exception();
        } catch(Exception e) { throw e; }
    }


    // Internal Methods to the Service
    @Cacheable(CacheConfig.TOKEN_HOLDERS_TOTAL)
    private Long getTotalTokenHolders(String contractAddr) { return tokenBalanceJpaRepository.getTotalHolders(contractAddr); }

    @Cacheable(CacheConfig.TOKEN_TRANSFERS_TOTAL)
    private Long getTotalTokenTransfers(String contractAddr) { return transferJpaRepository.getTotalTransfers(contractAddr); }

    @Cacheable(CacheConfig.TOKEN_HOLDERS_BY_CONTRACT_ADDRESS)
    private String getTokenHoldersByContractAddress(String searchParam, int holderPageNumber, int holderPageSize) throws Exception {
        if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
        Page<TokenBalance> tokenBalancePage = tokenBalanceJpaRepository.getTopHoldersByContractAddr(searchParam, new PageRequest(holderPageNumber, min(holderPageSize, 10000)));
        JSONObject holdersObject = new JSONObject();
        JSONArray holdersArray = new JSONArray();

        if(Utility.validHex(searchParam)) {
            if (tokenBalancePage != null) {
                List<TokenBalance> tokenBalanceList = tokenBalancePage.getContent();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                // Writing the Page/List of Tokens to a JSON Object
                if (tokenBalanceList != null && tokenBalanceList.size() > 0) {
                    for (TokenBalance tokenBalance : tokenBalanceList) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(tokenBalance));
                        holdersArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", tokenBalancePage.getTotalElements());
                    pageObject.put("totalPages", tokenBalancePage.getTotalPages());
                    pageObject.put("number", holderPageNumber);
                    pageObject.put("size", holderPageSize);

                    holdersObject.put("content", holdersArray);
                    holdersObject.put("page", pageObject);
                }
            }
        }

        if(holdersArray.length() == 0) return new JSONObject(JSONObject.NULL).toString(); // If there are no Holders

        return holdersObject.toString();
    }

    @Cacheable(CacheConfig.TOKEN_TRANSFERS_BY_CONTRACT_ADDRESS)
    private String getTokenTransfersByContractAddress(String searchParam, int transferPageNumber, int transferPageSize) throws Exception {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONObject transfersObject = new JSONObject();
        JSONArray transfersArray = new JSONArray();

        // Confirm as a valid hex
        if (Utility.validHex(searchParam)) {

            // transactions
            Page<Transfer> transferPage = transferJpaRepository.getLatestTransfersByContractAddr(searchParam, new PageRequest(transferPageNumber, transferPageSize));
            if (transferPage != null) {
                List<Transfer> transferList = transferPage.getContent();
                if (transferList != null && transferList.size() > 0) {
                    for (Transfer transfer : transferList) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(transfer));
                        transfersArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", transferPage.getTotalElements());
                    pageObject.put("totalPages", transferPage.getTotalPages());
                    pageObject.put("number", transferPageNumber);
                    pageObject.put("size", transferPageSize);

                    transfersObject.put("content", transfersArray);
                    transfersObject.put("page", pageObject);
                }
            }
        }

        if (transfersArray.length() == 0) return new JSONObject().put("rel", JSONObject.NULL).toString();   // If there are no Transfers

        return transfersObject.toString();
    }
}

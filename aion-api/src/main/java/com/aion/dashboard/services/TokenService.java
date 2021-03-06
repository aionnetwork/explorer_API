package com.aion.dashboard.services;

import static java.lang.Math.min;

import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenHolders;
import com.aion.dashboard.entities.TokenTransfers;
import com.aion.dashboard.repositories.TokenHoldersJpaRepository;
import com.aion.dashboard.repositories.TokenJpaRepository;
import com.aion.dashboard.repositories.TokenTransfersJpaRepository;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class TokenService {

    @Autowired
    private TokenJpaRepository tknRepo;

    @Autowired
    private TokenTransfersJpaRepository txfRepo;

    @Autowired
    private TokenHoldersJpaRepository tknHolRepo;


    private static final  String TOTAL_ELEMENTS="totalElements";
    private static final String TOTAL_PAGES="totalPages";
    private static final String NUMBER="number";
    private static final String CONTENT="content";

    public String getTokenList(long start,
                               long end,
                               int pageNumber,
                               int pageSize) throws Exception {
        JSONArray tknArray = new JSONArray();
        JSONObject tknObject = new JSONObject();

        var startInstant = Instant.ofEpochSecond(start).atZone(ZoneId.of("UTC"));
        var endInstant = Instant.ofEpochSecond(end).atZone(ZoneId.of("UTC"));

        Page<Token> tknPage = startInstant.getYear() == endInstant.getYear() ?
                tknRepo.findByYearAndMonthBetweenAndCreationTimestampBetween(
                        startInstant.getYear(),
                        startInstant.getMonthValue(),
                        endInstant.getMonthValue(),
                        start,
                        end,
                        PageRequest.of(pageNumber, pageSize))
                : tknRepo.findByYearBetweenAndCreationTimestampBetween(
                        startInstant.getYear(),
                        endInstant.getYear(),
                        start,
                        end,
                PageRequest.of(pageNumber, pageSize));

        List<Token> tknList = tknPage.getContent();
        if(tknList != null && !tknList.isEmpty()) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            for(Token tkn: tknList) {
                JSONObject result = new JSONObject(ow.writeValueAsString(tkn));
                tknArray.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, tknPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, tknPage.getTotalPages());
            pageObject.put(NUMBER, pageNumber);
            pageObject.put("size", pageSize);
            pageObject.put("start", start);
            pageObject.put("end", end);

            tknObject.put(CONTENT, tknArray);
            tknObject.put("page", pageObject);
        }

        // If the ResultSet is Null
        if (tknArray.length() == 0) {
            tknArray = new JSONArray();
            tknObject.put(CONTENT, tknArray);
        }

        return tknObject.toString();
    }

    public String getTokenListByTokenName(String tokenName,
                                          int pageNumber,
                                          int pageSize) throws Exception {
        JSONArray tokenArray = new JSONArray();
        JSONObject tokenObject = new JSONObject();

        Page<Token> tokenPage = tknRepo.findByName(tokenName, PageRequest.of(pageNumber, pageSize));                                   // Getting the Token Details by Token Name
        List<Token> tokenList = tokenPage.getContent();

        // Writing the Page/List of Tokens to a JSON Object
        if(!tokenList.isEmpty()) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            for (Token token : tokenList) {
                JSONObject result = new JSONObject(ow.writeValueAsString(token));
                result.put("totalHolders", getTotalTokenHolders(token.getContractAddr()));
                result.put("totalTransfers", getTotalTokenTransfers(token.getContractAddr()));
                tokenArray.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, tokenPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, tokenPage.getTotalPages());
            pageObject.put(NUMBER, tokenPage.getNumber());
            pageObject.put("size", tokenPage.getSize());

            tokenObject.put(CONTENT, tokenArray);
            tokenObject.put("page", pageObject);
        }

        // If the ResultSet is Null
        if(tokenArray.length() == 0) {
            tokenArray = new JSONArray();
            tokenObject.put(CONTENT, tokenArray);
        }

        return tokenObject.toString();
    }


    public String getTokenListByTokenSymbol(String tokenSymbol,
                                            int pageNumber,
                                            int pageSize) throws Exception {
        JSONArray tokenArray = new JSONArray();
        JSONObject tokenObject = new JSONObject();

        Page<Token> tokenPage = tknRepo.findBySymbol(tokenSymbol, PageRequest.of(pageNumber, pageSize));                                            // Getting the Token Details by Token Symbol
        List<Token> tokenList = tokenPage.getContent();

        // Writing the Page/List of Tokens to a JSON Object
        if(!tokenList.isEmpty()) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            for (Token token : tokenList) {
                JSONObject result = new JSONObject(ow.writeValueAsString(token));
                result.put("totalHolders", getTotalTokenHolders(token.getContractAddr()));
                result.put("totalTransfers", getTotalTokenTransfers(token.getContractAddr()));
                tokenArray.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, tokenPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, tokenPage.getTotalPages());
            pageObject.put(NUMBER, pageNumber);
            pageObject.put("size", pageSize);

            tokenObject.put(CONTENT, tokenArray);
            tokenObject.put("page", pageObject);
        }

        // If the ResultSet is Null
        if(tokenArray.length() == 0) {
            tokenArray = new JSONArray();
            tokenObject.put(CONTENT, tokenArray);
        }

        return tokenObject.toString();
    }

    public String getTokenDetailsTransfersAndHoldersByContractAddress(long start,
                                                                      long end,
                                                                      int holderPageNumber,
                                                                      int holderPageSize,
                                                                      int transferPageNumber,
                                                                      int transferPageSize,
                                                                      String contractAddr) throws Exception {
        if(contractAddr.startsWith("0x")) contractAddr = contractAddr.replace("0x", "");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray tokenArray = new JSONArray();

        if(Utility.isValidAddress(contractAddr)) {
            Token token = tknRepo.findByContractAddr(contractAddr);                                      // Getting the Token Details by Contract Address
            if (token != null) {
                JSONObject result = new JSONObject(ow.writeValueAsString(token));
                result.put("holders", new JSONObject(getTokenHoldersByContractAddress(
                        token.getContractAddr(),
                        holderPageNumber,
                        holderPageSize
                )));
                result.put("transfers", new JSONObject(getTokenTransfersByContractAddress(
                        start,
                        end,
                        transferPageNumber,
                        transferPageSize,
                        token.getContractAddr()
                )));
                tokenArray.put(result);
            }

            // If the ResultSet is Null
            if(tokenArray.length() == 0) {
                tokenArray = new JSONArray();
            }

            return new JSONObject().put(CONTENT, tokenArray).toString();
        }

        throw new Exception();
    }

    // Internal Methods to the Service
    private Long getTotalTokenHolders(String contractAddr) { return tknHolRepo.countByContractAddr(contractAddr); }

    private Long getTotalTokenTransfers(String contractAddr) { return txfRepo.countByContractAddr(contractAddr); }

    private String getTokenHoldersByContractAddress(String contractAddress,
                                                    int holderPageNumber,
                                                    int holderPageSize) throws Exception {
        Page<TokenHolders> tknHolsPage = tknHolRepo.findAllByContractAddr(contractAddress, PageRequest.of(holderPageNumber, min(holderPageSize, 10000), new Sort(Sort.Direction.DESC, "tknBalance")));
        List<TokenHolders> tknHolsList = tknHolsPage.getContent();

        JSONObject holdersObject = new JSONObject();
        JSONArray holdersArray = new JSONArray();

        if(Utility.validHex(contractAddress)) {

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        // Writing the Page/List of Tokens to a JSON Object
        if (tknHolsList != null && !tknHolsList.isEmpty()) {
            for (TokenHolders tokenHolders : tknHolsList) {
                JSONObject result = new JSONObject(ow.writeValueAsString(tokenHolders));
                holdersArray.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, tknHolsPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, tknHolsPage.getTotalPages());
            pageObject.put(NUMBER, holderPageNumber);
            pageObject.put("size", holderPageSize);

            holdersObject.put(CONTENT, holdersArray);
            holdersObject.put("page", pageObject);
        }

        // If the ResultSet is Null
        if (holdersArray.length() == 0) {
            holdersArray = new JSONArray();
            holdersObject.put(CONTENT, holdersArray);
        }

        return holdersObject.toString();
    }

        throw new Exception();
    }

    private String getTokenTransfersByContractAddress(long start,
                                                      long end,
                                                      int pageNumber,
                                                      int pageSize,
                                                      String contractAddress) throws Exception {
        if (Utility.validHex(contractAddress)) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONObject transfersObject = new JSONObject();
        JSONArray transfersArray = new JSONArray();

        var startInstant = Instant.ofEpochSecond(start).atZone(ZoneId.of("UTC"));
        var endInstant = Instant.ofEpochSecond(end).atZone(ZoneId.of("UTC"));

        Page<TokenTransfers> txfsPage = startInstant.getYear() == endInstant.getYear() ?
                txfRepo.findByYearAndMonthBetweenAndTransferTimestampBetweenAndContractAddr(
                        startInstant.getYear(),
                        startInstant.getMonthValue(),
                        endInstant.getMonthValue(),
                        start,
                        end,
                        contractAddress,
                        PageRequest.of(pageNumber, pageSize))
                : txfRepo.findByYearBetweenAndTransferTimestampBetweenAndContractAddr(
                        startInstant.getYear(),
                        endInstant.getYear(),
                        start,
                        end,
                        contractAddress,
                PageRequest.of(pageNumber, pageSize));

        List<TokenTransfers> txfsList = txfsPage.getContent();
        if (txfsList != null && !txfsList.isEmpty()) {
            for (TokenTransfers tokenTransfers : txfsList) {
                JSONObject result = new JSONObject(ow.writeValueAsString(tokenTransfers));
                transfersArray.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, txfsPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, txfsPage.getTotalPages());
            pageObject.put(NUMBER, pageNumber);
            pageObject.put("size", pageSize);
            pageObject.put("start", start);
            pageObject.put("end", end);

            transfersObject.put(CONTENT, transfersArray);
            transfersObject.put("page", pageObject);
        }

        // If the ResultSet is Null
        if (transfersArray.length() == 0) {
            transfersArray = new JSONArray();
            transfersObject.put(CONTENT, transfersArray);
        }

            return transfersObject.toString();
        }

        throw new Exception();
    }
}
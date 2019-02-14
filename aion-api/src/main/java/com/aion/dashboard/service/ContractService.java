package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.*;
import com.aion.dashboard.repository.*;
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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;

@Component
public class ContractService {

    @Autowired
    EventJpaRepository eventJpaRepository;

    @Autowired
    TokenJpaRepository tokenJpaRepository;

    @Autowired
    BalanceJpaRepository balanceJpaRepository;

    @Autowired
    ContractJpaRepository contractJpaRepository;

    @Autowired
    ParserStateJpaRepository parserStateJpaRepository;

    @Autowired
    TransactionJpaRepository transactionJpaRepository;

    private final static String ATB_ADDRESS = "0000000000000000000000000000000000000000000000000000000000000200";

    @Cacheable(CacheConfig.CONTRACT_LIST)
    public String getContractList(int pageNumber, int pageSize) throws Exception {
        try {
            JSONArray contractArray = new JSONArray();
            JSONObject contractObject = new JSONObject();
            Page<Contract> contractPage = contractJpaRepository.findAllContracts(new PageRequest(pageNumber, min(pageSize, 1000)));

            if(contractPage != null) {
                List<Contract> contractList = contractPage.getContent();
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

                // Writing the Page/List of Tokens to a JSON Object
                if(contractList != null && contractList.size() > 0) {
                    for (Contract contract : contractList) {
                        JSONObject result = new JSONObject(ow.writeValueAsString(contract));
                        result.put("totalTransactions", 0);
                        result.put("totalAccounts", 0);
                        contractArray.put(result);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", contractPage.getTotalElements());
                    pageObject.put("totalPages", contractPage.getTotalPages());
                    pageObject.put("number", contractPage.getNumber());
                    pageObject.put("size", contractPage.getSize());

                    contractObject.put("content", contractArray);
                    contractObject.put("page", pageObject);

                    return contractObject.toString();
                }
            }

            return new JSONObject().put("rel", JSONObject.NULL).toString();
        } catch(Exception e) { throw e; }
    }

    @Cacheable(CacheConfig.CONTRACT_DETAIL_BY_CONTRACT_ADDRESS)
    public String getContractDetailsByContractAddress(String searchParam, int eventPageNumber, int eventPageSize, int transactionPageNumber, int transactionPageSize) throws Exception {
        try {
            if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONArray contractArray = new JSONArray();

            if(Utility.validHex(searchParam)) {
                Contract contract = contractJpaRepository.findByContractAddr(searchParam);
                if(contract != null) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(contract));

                    // Checking for the Balance and Nonce of the Contract
                    Balance balance = balanceJpaRepository.findByAddress(searchParam);
                    if(balance != null && balance.isContract()) {
                        result.put("nonce", balance.getNonce());
                        result.put("balance", balance.getBalance());
                    }

                    // Checking if the Contract is the Aion Token Bridge (ATB)
                    if(contract.getContractAddr().equals(ATB_ADDRESS)) {
                        result.put("isATB", true);
                    } else {
                        result.put("isATB", false);
                    }

                    // Checking if the Contract is a Token
                    if(tokenJpaRepository.findByContractAddr(contract.getContractAddr()) != null) result.put("isToken", true);
                    else result.put("isToken", false);

                    // Getting Contract's Events and Transactions
                    result.put("events", new JSONObject(getContractEventsByContractAddress(contract.getContractAddr(), eventPageNumber, Math.min(eventPageSize, 1000))));
                    result.put("transactions", new JSONObject(getContractTransactionsByContractAddress(contract.getContractAddr(), transactionPageNumber, Math.min(transactionPageSize, 1000))));

                    contractArray.put(result);
                    return new JSONObject().put("content", contractArray).toString();
                }
            }

            throw new Exception();
        } catch(Exception e) { throw e; }
    }


    // Internal Methods
    @Cacheable(CacheConfig.CONTRACT_EVENTS_BY_CONTRACT_ADDRESS)
    private String getContractEventsByContractAddress(String contractAddress, int pageNumber, int pageSize) throws Exception {
        try {
            // Gathering the Events of the Contract ...
            Page<Event> eventPage = eventJpaRepository.findByContractAddr(contractAddress, new PageRequest(pageNumber, Math.min(pageSize, 1000)));
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject eventObject = new JSONObject();
            JSONArray eventArray = new JSONArray();
            if (eventPage != null) {
                List<Event> eventList = eventPage.getContent();
                if (eventList != null && eventList.size() > 0) {
                    for (Event event : eventList) {
                        JSONObject eventResult = new JSONObject(ow.writeValueAsString(event));
                        eventArray.put(eventResult);
                    }

                    JSONObject pageObject = new JSONObject();
                    pageObject.put("totalElements", eventPage.getTotalElements());
                    pageObject.put("totalPages", eventPage.getTotalPages());
                    pageObject.put("number", eventPage.getNumber());
                    pageObject.put("size", eventPage.getSize());

                    eventObject.put("page", pageObject);
                }
            }
            if (eventArray.length() == 0) return new JSONObject().put("rel", JSONObject.NULL).toString(); // If there are no Events

            eventObject.put("content", eventArray);
            return eventObject.toString();
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Cacheable(CacheConfig.CONTRACT_TRANSACTIONS_BY_CONTRACT_ADDRESS)
    private String getContractTransactionsByContractAddress(String contractAddress, int pageNumber, int pageSize) throws Exception {
        try {
            // Gathering Transactions of the Contract...
            Optional<ParserState> parserState = parserStateJpaRepository.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            JSONObject transactionObject = new JSONObject();
            JSONArray transactionArray = new JSONArray();
            if(parserState.isPresent()) {
                long transactionId = parserState.get().getTransactionId();
                Page<Transaction> transactionPage = transactionJpaRepository.getTransactionsForAddressBetween(transactionId - TransactionService.TRANSACTION_LIMIT, transactionId, contractAddress,
                        new PageRequest(pageNumber, Math.min(pageSize, 1000)));
                if(transactionPage != null) {
                    List<Transaction> transactionList = transactionPage.getContent();
                    if (transactionList != null && transactionList.size() > 0) {
                        for (int i = 0; i < transactionList.size(); i++) {
                            JSONObject transactionResult = new JSONObject(ow.writeValueAsString(transactionList.get(i)));

                            // String handling the Transaction Log of the Transaction


                            transactionArray.put(transactionResult);
                        }

                        JSONObject pageObject = new JSONObject();
                        pageObject.put("totalElements", transactionPage.getTotalElements());
                        pageObject.put("totalPages", transactionPage.getTotalPages());
                        pageObject.put("number", transactionPage.getNumber());
                        pageObject.put("size", transactionPage.getSize());

                        transactionObject.put("page", pageObject);
                    }
                }
            }
            if(transactionArray.length() == 0) return new JSONObject().put("rel", JSONObject.NULL).toString();  // If there are no Transactions

            transactionObject.put("content", transactionArray);
            return transactionObject.toString();
        } catch (Exception e) {
            throw new Exception();
        }
    }
}

package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.Contract;
import com.aion.dashboard.entities.Event;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repositories.*;
import com.aion.dashboard.utility.Logging;
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

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Component
public class ContractService {

    @Autowired
    EventJpaRepository evtRepo;

    @Autowired
    TokenJpaRepository tknRepo;

    @Autowired
    AccountJpaRepository acctRepo;

    @Autowired
    ContractJpaRepository conRepo;

    @Autowired
    TransactionJpaRepository txnRepo;

    private static final String TOTAL_ELEMENTS="totalElements";
    private static final String TOTAL_PAGES= "totalPages";
    private static final String NUMBER="number";
    private static final String CONTENT ="content";
    private static final  String ATB_ADDRESS = "0000000000000000000000000000000000000000000000000000000000000200";
    private Sort sortDesc(){
        return new Sort( Sort.Direction.DESC,"blockNumber");
    }
    @Cacheable(CacheConfig.CONTRACT_LIST)
    public String getContractList(long start,
                                  long end,
                                  int pageNumber,
                                  int pageSize) throws Exception {
        JSONArray conArray = new JSONArray();
        JSONObject conObject = new JSONObject();

        var startInstant = Instant.ofEpochSecond(start).atZone(ZoneId.of("UTC"));
        var endInstant = Instant.ofEpochSecond(end).atZone(ZoneId.of("UTC"));

        Logging.traceLogStartAndEnd(startInstant, endInstant, "GetContractList");
        Page<Contract> consPage = startInstant.getYear() == endInstant.getYear() ?
                conRepo.findByYearAndMonthBetweenAndDeployTimestampBetween(
                        startInstant.getYear(),
                        startInstant.getMonthValue(),
                        endInstant.getMonthValue(),
                        start,
                        end,
                        PageRequest.of(pageNumber, pageSize, sortDesc()))
                : conRepo.findByYearBetweenAndDeployTimestampBetween(
                        startInstant.getYear(),
                        endInstant.getYear(),
                        start,
                        end,
                        PageRequest.of(pageNumber, pageSize, sortDesc()));

        List<Contract> consList = consPage.getContent();
        if(consList != null && !consList.isEmpty()) {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            for(Contract blk: consList) {
                JSONObject result = new JSONObject(ow.writeValueAsString(blk));
                conArray.put(result);
            }


            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, consPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, consPage.getTotalPages());
            pageObject.put(NUMBER, pageNumber);
            pageObject.put("size", pageSize);
            pageObject.put("start", start);
            pageObject.put("end", end);

            conObject.put(CONTENT, conArray);
            conObject.put("page", pageObject);
        }

        // If the ResultSet is Null
        if (conArray.length() == 0) {
            conArray = new JSONArray();
            conObject.put(CONTENT, conArray);
        }

        return conObject.toString();
    }

    @Cacheable(CacheConfig.CONTRACT_DETAIL_BY_CONTRACT_ADDRESS)
    public String getContractDetailsByContractAddress(String searchParam,
                                                      int eventPageNumber,
                                                      int eventPageSize,
                                                      int transactionPageNumber,
                                                      int transactionPageSize) throws Exception {
        if(searchParam.startsWith("0x")) searchParam = searchParam.replace("0x", "");
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONArray contractArray = new JSONArray();

        if(Utility.isValidAddress(searchParam)) {
            Contract contract = conRepo.findByContractAddr(searchParam);
            if(contract != null) {
                JSONObject result = new JSONObject(ow.writeValueAsString(contract));

                // Checking for the Account and Nonce of the Contract
                Account account = acctRepo.findByAddress(searchParam);
                if(account != null && account.isContract()) {
                    result.put("nonce", account.getNonce());
                    result.put("account", account.getBalance());
                }

                // Checking if the Contract is the Aion Token Bridge (ATB)
                result.put("isATB", contract.getContractAddr().equals(ATB_ADDRESS));

                // Checking if the Contract is a Token
                result.put("isToken", tknRepo.findByContractAddr(contract.getContractAddr()) != null);

                // Getting Contract's Events and transactions
                result.put("events", new JSONObject(getContractEventsByContractAddress(contract.getContractAddr(), eventPageNumber, eventPageSize)));
                result.put("transactions", new JSONObject(getContractTransactionsByContractAddress(contract.getContractAddr(), transactionPageNumber, transactionPageSize)));


                result.put("balance", Utility.toAion(acctRepo.findByAddress(searchParam).getBalance()) );
                contractArray.put(result);
            }

            // If the ResultSet is Null
            if(contractArray.length() == 0) {
                contractArray = new JSONArray();
            }

            return new JSONObject().put(CONTENT, contractArray).toString();
        }

        throw new Exception();
    }

    // Internal Methods
    @Cacheable(CacheConfig.CONTRACT_EVENTS_BY_CONTRACT_ADDRESS)
    private String getContractEventsByContractAddress(String contractAddress,
                                                      int pageNumber,
                                                      int pageSize) throws Exception {
        JSONArray eventArray = new JSONArray();
        JSONObject eventObject = new JSONObject();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Page<Event> eventPage = evtRepo.findByContractAddr(contractAddress, PageRequest.of(pageNumber, Math.min(pageSize, 1000)));
        List<Event> eventList = eventPage.getContent();
        if (eventList != null && !eventList.isEmpty()) {
            for (Event event : eventList) {
                JSONObject eventResult = new JSONObject(ow.writeValueAsString(event));
                eventArray.put(eventResult);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, eventPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, eventPage.getTotalPages());
            pageObject.put(NUMBER, eventPage.getNumber());
            pageObject.put("size", eventPage.getSize());

            eventObject.put("page", pageObject);
            eventObject.put(CONTENT, eventArray);
        }

        // If the ResultSet is null
        if (eventArray.length() == 0) {
            eventArray = new JSONArray();
            eventObject.put(CONTENT, eventArray);
        }

        return eventObject.toString();
    }

    @Cacheable(CacheConfig.CONTRACT_TRANSACTIONS_BY_CONTRACT_ADDRESS)
    private String getContractTransactionsByContractAddress(String contractAddress,
                                                            int pageNumber,
                                                            int pageSize) throws Exception {
        JSONArray transactionArray = new JSONArray();
        JSONObject transactionObject = new JSONObject();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Page<Transaction> transactionPage = txnRepo.findTransactionsByAddress(contractAddress, PageRequest.of(pageNumber, pageSize));
        List<Transaction> transactionList = transactionPage.getContent();
        if (transactionList != null && !transactionList.isEmpty()) {
            for (Transaction transaction : transactionList) {
                JSONObject transactionResult = new JSONObject(ow.writeValueAsString(transaction));
                transactionResult.put("value", Utility.toAion(transaction.getValue()));
                transactionArray.put(transactionResult);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put(TOTAL_ELEMENTS, transactionPage.getTotalElements());
            pageObject.put(TOTAL_PAGES, transactionPage.getTotalPages());
            pageObject.put(NUMBER, transactionPage.getNumber());
            pageObject.put("size", transactionPage.getSize());

            transactionObject.put("page", pageObject);
            transactionObject.put(CONTENT, transactionArray);
        }

        // If the ResultSet is Null
        if(transactionArray.length() == 0) {
            transactionArray = new JSONArray();
            transactionObject.put(CONTENT, transactionArray);
        }

        return transactionObject.toString();
    }
}
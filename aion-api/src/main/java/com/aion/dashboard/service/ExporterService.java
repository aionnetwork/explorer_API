package com.aion.dashboard.service;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repository.*;
import com.aion.dashboard.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ExporterService {

    @Autowired
    private BlockJpaRepository blockJpaRepository;

    @Autowired
    private TokenJpaRepository tokenJpaRepository;

    @Autowired
    private BalanceJpaRepository balanceJpaRepository;

    @Autowired
    private ContractJpaRepository contractJpaRepository;

    @Autowired
    private GraphingJpaRepository graphingJpaRepository;

    @Autowired
    private TransactionJpaRepository transactionJpaRepository;

    @Autowired
    private ParserStateJpaRepository parserStateJpaRepository;

    @Autowired
    private TokenBalanceJpaRepository tokenBalanceJpaRepository;

    @Cacheable(CacheConfig.ACCOUNT_TKNS_TO_CSV)
    public String exportToCSV(String searchParam1,
                              String searchParam2,
                              String entityType,
                              long min1,
                              long max1,
                              long min2,
                              long max2,
                              HttpServletResponse response) throws Exception {
        try {
            DateFormat formatter = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
            String fileName = entityType + "-" + formatter.format(new Date(System.currentTimeMillis())) + ".csv";

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + fileName;
            response.setHeader(headerKey, headerValue);
            response.setContentType("text/csv");

            StringBuilder data = new StringBuilder();
            List<String> header = new ArrayList<>();

            if(min1 > max1 || min2 > max2) throw new Exception();

            // Transforming the data to a its Entity Object based on its EntityType
            switch(entityType) {

                case "Account":
                    if(searchParam1 == null || searchParam1.trim().isEmpty() || !Utility.validHex(searchParam1)) throw new Exception();
                    List<Transaction> transactionList = transactionJpaRepository.findTransactionsByAddress(searchParam1);

                    header.add("Block,Timestamp,Value (in Aion),Tx Hash,From,To");
                    data.insert(0, header.get(0));
                    for (Transaction transaction : transactionList) {
                        data.append("\n")
                                .append(transaction.getBlockNumber()).append(",")
                                .append(transaction.getBlockTimestamp()).append(",")
                                .append(Utility.fromNano(transaction.getValue()).doubleValue()).append(",")
                                .append(transaction.getTransactionHash()).append(",")
                                .append(transaction.getFromAddr()).append(",")
                                .append(transaction.getToAddr());
                    }

                    break;
            }

            return data.toString().replace("[", "").replace("]","");
        } catch (Exception e) { throw e; }
    }

}
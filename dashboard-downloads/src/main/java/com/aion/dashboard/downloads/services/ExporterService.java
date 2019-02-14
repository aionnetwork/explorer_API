package com.aion.dashboard.downloads.services;

import com.aion.dashboard.downloads.configs.CacheConfig;
import com.aion.dashboard.downloads.entities.TokenTransfers;
import com.aion.dashboard.downloads.entities.Transaction;
import com.aion.dashboard.downloads.repositories.*;
import com.aion.dashboard.downloads.specifications.TransactionSpec;
import com.aion.dashboard.downloads.specifications.TransferSpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ExporterService {

    @Autowired
    private BlockJpaRepository blkRepo;

    @Autowired
    private TokenJpaRepository tknRepo;

    @Autowired
    private EventJpaRepository evtRepo;

    @Autowired
    private AccountJpaRepository acctRepo;

    @Autowired
    private ContractJpaRepository conRepo;

    @Autowired
    private TokenTransfersJpaRepository tknTxfRepo;

    @Autowired
    private TransactionJpaRepository txnRepo;

    @Autowired
    private TokenHoldersJpaRepository tknHolRepo;

    @Autowired
    private InternalTransferJpaRepository iTxfRepo;

    /*
     * Parses out the Transactions an Account is involved in
     * Into a .csv file to be downloaded
     */
    @Cacheable(CacheConfig.ACCOUNT_TXNS_TO_CSV)
    private String accountTxnsToCsv(String accountAddress,
                                    long timestampStart,
                                    long timestampEnd) {
        StringBuilder data = new StringBuilder();

        // Getting the Start Month and Start Year from the Start Timestamp
        int startMonth = LocalDate.ofInstant(Instant.ofEpochSecond(timestampStart), ZoneId.of("UTC")).getMonthValue();
        int startYear = LocalDate.ofInstant(Instant.ofEpochSecond(timestampStart), ZoneId.of("UTC")).getYear();

        // Getting the End Month and End Year from the End Timestamp
        int endMonth = LocalDate.ofInstant(Instant.ofEpochSecond(timestampEnd), ZoneId.of("UTC")).getMonthValue();
        int endYear = LocalDate.ofInstant(Instant.ofEpochSecond(timestampEnd), ZoneId.of("UTC")).getYear();

        // If the request is for Transactions involving an Account w/ Native AION

        List<Transaction> txnsList = startYear == endYear ?
                    txnRepo.findAll(TransactionSpec.hasAddr(accountAddress).and(TransactionSpec.checkTime(startYear, startMonth, endMonth, timestampStart, timestampEnd)), new Sort(Sort.Direction.DESC, "blockTimestamp"))
                    : txnRepo.findAll(TransactionSpec.hasAddr(accountAddress).and(TransactionSpec.checkTime(startYear, endYear, timestampStart, timestampEnd)), new Sort(Sort.Direction.DESC, "blockTimestamp"));
        
        if (!txnsList.isEmpty()) {
            data.append("Block,Date,Value (in Aion),Txn Hash,From,To");
            for (Transaction txn : txnsList) {
                ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(txn.getBlockTimestamp()), ZoneId.of("UTC"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
                data.append("\n")
                        .append(txn.getBlockNumber()).append(",")
                        .append(dateTime.format(formatter)).append(",")
                        .append(txn.getValue()).append(",")
                        .append(txn.getTransactionHash()).append(",")
                        .append(txn.getFromAddr()).append(",")
                        .append(txn.getToAddr());
            }
        } else return null;

        return data.toString();
    }

    @Cacheable(CacheConfig.ACCOUNT_TXNS_TO_CSV)
    private String accountTxnsToCsv(String accountAddress,
                                    String tokenAddress,
                                    long timestampStart,
                                    long timestampEnd) {
        StringBuilder data = new StringBuilder();

        // Getting the Start Month and Start Year from the Start Timestamp
        int startMonth = LocalDate.ofInstant(Instant.ofEpochSecond(timestampStart), ZoneId.of("UTC")).getMonthValue();
        int startYear = LocalDate.ofInstant(Instant.ofEpochSecond(timestampStart), ZoneId.of("UTC")).getYear();

        // Getting the End Month and End Year from the End Timestamp
        int endMonth = LocalDate.ofInstant(Instant.ofEpochSecond(timestampEnd), ZoneId.of("UTC")).getMonthValue();
        int endYear = LocalDate.ofInstant(Instant.ofEpochSecond(timestampEnd), ZoneId.of("UTC")).getYear();

        // If the request is for Transactions involving an Account w/ a Specific Token
        if(tokenAddress.startsWith("0x")) tokenAddress = tokenAddress.replace("0x", "");
        List<TokenTransfers> txfsList = startYear == endYear ?
                tknTxfRepo.findAll(TransferSpec.hasAddr(accountAddress).and(TransferSpec.checkTime(startYear, startMonth, endMonth, timestampStart, timestampEnd)).and(TransferSpec.tokenIs(tokenAddress)), new Sort(Sort.Direction.DESC, "blockNumber"))
                : tknTxfRepo.findAll(TransferSpec.hasAddr(accountAddress).and(TransferSpec.checkTime(startYear, endYear, timestampStart, timestampEnd)).and(TransferSpec.tokenIs(tokenAddress)), new Sort(Sort.Direction.DESC, "blockNumber"));
        if(!txfsList.isEmpty()) {
            data.append("Block,Date,Value (in Token),Txn Hash,From,To");
            for (TokenTransfers txf : txfsList) {
                ZonedDateTime dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(txf.getTransferTimestamp()), ZoneId.of("UTC"));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
                data.append("\n")
                        .append(txf.getBlockNumber()).append(",")
                        .append(dateTime.format(formatter)).append(",")
                        .append(txf.getTknValue()).append(",")
                        .append(txf.getTransactionHash()).append(",")
                        .append(txf.getFromAddr()).append(",")
                        .append(txf.getToAddr());
            }
        } else return null;

        return data.toString();
    }

    public String exportToCSV(String searchParam1,
                              String searchParam2,
                              String entityType,
                              long timeStart,
                              long timeEnd,
                              HttpServletResponse response) {
        Instant time = new Timestamp(System.currentTimeMillis()).toInstant();
        String pattern = "dd/MM/yyyy hh:mm:ss a";
        ZonedDateTime date = ZonedDateTime.ofInstant(time, ZoneOffset.UTC);

        String fileName = entityType + "-" + date.format(DateTimeFormatter.ofPattern(pattern)) + ".csv";

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
        response.setContentType("text/csv");


        // Map the data to be streamed to the relevant  method based on the entityType variable
        if ("Account-Transactions".equals(entityType)) {
            if (searchParam1.startsWith("0x")) searchParam1 = searchParam1.replace("0x", "");
            if (searchParam2 == null) return accountTxnsToCsv(searchParam1, timeStart, timeEnd);
            else return accountTxnsToCsv(searchParam1, searchParam2, timeStart, timeEnd);
        }

        return null;
    }
}
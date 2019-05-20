package com.aion.dashboard.services;

import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.exception.MissingArgumentException;
import com.aion.dashboard.repositories.*;
import com.aion.dashboard.utility.Utility;
import com.aion.dashboard.view.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class SearchImpMysql implements SearchService {
    private static final SearchResult EMPTY_RESULT;
    static {
        EMPTY_RESULT = new SearchResult(List.of());
    }
    private final ExecutorService dbExecutor;
    private TokenJpaRepository tknRepo;
    private AccountJpaRepository acctRepo;
    private ParserStateJpaRepository pSRepo;
    private BlockJpaRepository blkRepo;
    private TransactionJpaRepository txRepo;
    private ContractJpaRepository contractRepo;


    @Autowired
    SearchImpMysql(TokenJpaRepository tknRepo,
                   AccountJpaRepository acctRepo,
                   ParserStateJpaRepository pSRepo,
                   BlockJpaRepository blkRepo,
                   TransactionJpaRepository txRepo,
                   ContractJpaRepository contractRepo ) {

        this.tknRepo = tknRepo;
        this.acctRepo = acctRepo;
        this.pSRepo = pSRepo;
        this.blkRepo = blkRepo;
        this.txRepo = txRepo;
        this.contractRepo = contractRepo;
        this.dbExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    }

    @Override
    public SearchResult search(final String key) {

        if (key == null || key.isEmpty() || key.isBlank()) {
            throw new MissingArgumentException();// thrown if no argument is supplied
        } else {
            final String searchKey = key.strip().replaceFirst("0x","");
            //Fork and run all task asynchronously
            CompletableFuture<SearchResult> blockFuture = CompletableFuture.supplyAsync(() -> searchBlock(searchKey), dbExecutor);
            CompletableFuture<SearchResult> transactionFuture = CompletableFuture.supplyAsync(() -> searchTransaction(searchKey), dbExecutor);
            CompletableFuture<SearchResult> accountFuture = CompletableFuture.supplyAsync(() -> searchAccount(searchKey), dbExecutor);
            CompletableFuture<SearchResult> tokenFuture = CompletableFuture.supplyAsync(() -> searchToken(searchKey), dbExecutor);
            CompletableFuture<SearchResult> contractFuture = CompletableFuture.supplyAsync(() -> searchContract(searchKey), dbExecutor);

            return blockFuture
                    .thenCombine(transactionFuture, SearchResult::merge) // join the results from the block table with the transaction
                    .thenCombine(accountFuture, SearchResult::merge)// join the results from the account table with the previous
                    .thenCombine(tokenFuture, SearchResult::merge)// join the results from the token table with the previous
                    .thenCombine(contractFuture, SearchResult::merge) // join the results from the contract table with the previous
                    .join(); // block then return the completed result
        }

    }



    private SearchResult searchBlock(String key) {

        if (Utility.validLong(key)){// check if the key is a block number
            var containsBlock =  pSRepo.findById(1)
                    .stream()
                    .mapToLong(ParserState::getBlockNumber)
                    .findAny()
                    .orElse(-1L) >= Long.parseLong(key);

            return containsBlock ? SearchResult.of("block", key) : EMPTY_RESULT;
        }
        else if (Utility.validHex(key)){//check if the key is a block hash
            var block = blkRepo.findByBlockHash(key);

            return block != null? SearchResult.of("block", block.getBlockNumber().toString()) : EMPTY_RESULT;
        }
        else  {
            return EMPTY_RESULT; // else return the default
        }
    }




    private SearchResult searchTransaction(String key){
        if (Utility.validHex(key)){ //check if the key exists in the transaction table
            var transaction = txRepo.findByTransactionHash(key);

            return transaction != null ? SearchResult.of("transaction", key): EMPTY_RESULT;
        }
        else {// else return an empty result
            return EMPTY_RESULT;
        }
    }




    private SearchResult searchAccount(String key){
        if (Utility.isValidAddress(key)){ //check if the key exists in the account table
            var acc = acctRepo.findByAddress(key);
            return acc != null ? SearchResult.of("account", key): EMPTY_RESULT;
        }
        else {
            return EMPTY_RESULT;
        }
    }



    private SearchResult searchContract (String key){
        if (Utility.isValidAddress(key)){ // check if the contract exists in the contract table
            return contractRepo.existsByContractAddr(key) ? SearchResult.of("contract", key): EMPTY_RESULT;
        }
        else {
            return EMPTY_RESULT;
        }
    }




    private SearchResult searchToken (String key){
        if (Utility.isValidAddress(key)){ // check if the contract can be found by its contract addr
            return tknRepo.existsByContractAddr(key)? SearchResult.of("token",key): EMPTY_RESULT;
        }
        else {// check if the contract can be found by name or symbol
            var tokens = tknRepo.findAllByNameOrSymbol(key,key);
            return tokens.isEmpty() ? EMPTY_RESULT :
                    SearchResult.of("token", (String[]) tokens.stream()
                            .map(Token::getContractAddr)
                            .collect(Collectors.toList())
                            .toArray(new String[]{}));
        }
    }

}

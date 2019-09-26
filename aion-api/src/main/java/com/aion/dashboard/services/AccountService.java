package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenHolders;
import com.aion.dashboard.repositories.AccountJpaRepository;
import com.aion.dashboard.repositories.InternalTransactionJpaRepository;
import com.aion.dashboard.repositories.InternalTransferJpaRepository;
import com.aion.dashboard.repositories.ParserStateJpaRepository;
import com.aion.dashboard.repositories.TokenHoldersJpaRepository;
import com.aion.dashboard.repositories.TokenJpaRepository;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.Utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    @Autowired
    private InternalTransferJpaRepository internalTransferJpaRepository;

    @Autowired
    private TokenJpaRepository tknRepo;

    @Autowired
    private AccountJpaRepository acctRepo;

    @Autowired
    private ParserStateJpaRepository pSRepo;

    @Autowired
    private TokenHoldersJpaRepository tknBalRepo;

    @Autowired
    private InternalTransactionJpaRepository internalTransactionJpaRepository;


    private static final   String CONTENT="content";
    private static final  String BALANCE="balance";

    private Sort sort(String sort){
        return Sort.by(Sort.Direction.fromString(sort), BALANCE);
    }

    @Cacheable(CacheConfig.ACCOUNT_DETAILS)
    public String getAccountDetails(String accountAddress,
                                    String tokenAddress) throws Exception {
        if (accountAddress.startsWith("0x")) accountAddress = accountAddress.replace("0x", "");

        Optional<ParserState> parserState = pSRepo.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONObject accountObject = new JSONObject();
        JSONArray accountArray = new JSONArray();

        if (Utility.isValidAddress(accountAddress) && parserState.isPresent()) {
            Account account = acctRepo.findByAddress(accountAddress);
            if (account != null) {
                JSONObject result = new JSONObject(ow.writeValueAsString(account));

                // Converting the Balance from Wei to Aion
                result.put(BALANCE, Utility.toAion(account.getBalance()));

                // Converting Nonce from String to BigInteger
                result.put("nonce", new BigInteger(account.getNonce(), 16));

                // Getting the List of Tokens for the Account
                JSONArray tokensArray =  new JSONArray(getAccountTokenList(account.getAddress()));
                result.put("tokens", tokensArray);


                result.put("hasInternalTransfer", internalTransactionJpaRepository.existsByAddr(accountAddress));

                // Getting the LastBlockNumber from the Parser State
                result.put("lastBlockNumber", parserState.get().getBlockNumber());

                // Getting Token Information if Entered and Found
                if (tokenAddress != null) {
                    if(tokenAddress.startsWith("0x")) tokenAddress = tokenAddress.replace("0x", "");
                    TokenHolders tokenHolders = tknBalRepo.findByContractAddrAndHolderAddr(tokenAddress, accountAddress);
                    if (tokenHolders != null) {
                        result.put(BALANCE, tokenHolders.getRawBalance());
                        Token token = tknRepo.findByContractAddr(tokenAddress);
                        if (token != null) {
                            result.put("tokenName", token.getName());
                            result.put("tokenSymbol", token.getSymbol());
                        }
                    } else throw new Exception();
                }

                accountArray.put(result);
                accountObject.put(CONTENT, accountArray);
            }

            // If the ResultSet is Null
            if(accountArray.length() == 0) {
                accountArray = new JSONArray();
                accountObject.put(CONTENT, accountArray);
            }

            return accountObject.toString();
        }

        throw new Exception();
    }

    @Cacheable(CacheConfig.ACCOUNT_RICH_LIST)
    public String getAccountRichList() {
        JSONObject accountObj = new JSONObject();
        JSONArray accountArr = new JSONArray();

        Page<Account> accountPage = acctRepo.getRichList(PageRequest.of(0, 25, new Sort(Sort.Direction.DESC, BALANCE)));
        List<Account> accountList = accountPage.getContent();
        if(!accountList.isEmpty()) {
            for(Account account : accountList) {
                JSONObject result = new JSONObject().put("address", account.getAddress());

                // Converting Balance from Wei to Aion
                result.put(BALANCE, Utility.toAion(account.getBalance()));
                accountArr.put(result);
            }

            JSONObject pageObject = new JSONObject();
            pageObject.put("totalElements", accountPage.getTotalElements());
            pageObject.put("totalPages", accountPage.getTotalPages());
            pageObject.put("number", 0);
            pageObject.put("size", 25);

            accountObj.put(CONTENT, accountArr);
            accountObj.put("page", pageObject);

        }

        // If the ResultSet is Null
        if(accountArr.length() == 0) {
            accountArr = new JSONArray();
            accountObj.put(CONTENT, accountArr);
        }

        return accountObj.toString();
    }



    // Internal Methods
    @Cacheable(CacheConfig.ACCOUNT_TOKEN_LIST)
    private String getAccountTokenList(String holderAddress) throws Exception {
        List<TokenHolders> tokenHoldersList = tknBalRepo.findByHolderAddr(holderAddress);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        JSONArray tokenArray = new JSONArray();
        if (tokenHoldersList != null && !tokenHoldersList.isEmpty()) {
            for (TokenHolders tokenHolders : tokenHoldersList) {
                Token token = tknRepo.findByContractAddr(tokenHolders.getContractAddr());
                if (token != null) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(token));
                    tokenArray.put(result);
                }
            }
        }

        return tokenArray.toString();
    }

    public Page<Account> findAccounts(int page, int size, String sort){
        return acctRepo.findAll(PageRequest.of(page, size, sort(sort)));
    }

    public Account findByAccountAddress(String address){
        return acctRepo.findById(address).orElseThrow();
    }
}
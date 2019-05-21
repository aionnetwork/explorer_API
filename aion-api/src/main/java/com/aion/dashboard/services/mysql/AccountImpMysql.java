package com.aion.dashboard.services.mysql;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenHolders;
import com.aion.dashboard.repositories.AccountJpaRepository;
import com.aion.dashboard.services.*;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Component
public class AccountImpMysql implements AccountService {

    @Autowired
    private InternalTransferService internalTransferService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    private ParserStateService parserStateService;

    @Autowired
    private TokenHoldersService tokenHoldersService;


    private static final   String CONTENT="content";
    private static final  String BALANCE="balance";

    @Cacheable(CacheConfig.ACCOUNT_DETAILS)
    @Override
    public String getAccountDetails(String accountAddress,
                                    String tokenAddress) throws Exception {
        if (accountAddress.startsWith("0x")) accountAddress = accountAddress.replace("0x", "");

        Optional<ParserState> parserState = parserStateService.findById(ParserStateType.HEAD_BLOCK_TABLE.getId());
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        JSONObject accountObject = new JSONObject();
        JSONArray accountArray = new JSONArray();

        if (Utility.isValidAddress(accountAddress) && parserState.isPresent()) {
            Account account = accountJpaRepository.findByAddress(accountAddress);
            if (account != null) {
                JSONObject result = new JSONObject(ow.writeValueAsString(account));

                // Converting the Balance from Wei to Aion
                result.put(BALANCE, Utility.toAion(account.getBalance()));

                // Converting Nonce from String to BigInteger
                result.put("nonce", new BigInteger(account.getNonce(), 16));

                // Getting the List of Tokens for the Account
                JSONArray tokensArray =  new JSONArray(getAccountTokenList(account.getAddress()));
                result.put("tokens", tokensArray);


                var internalTransferRecord = internalTransferService.findTopByToAddrOrFromAddr(accountAddress, accountAddress);// find only one
                result.put("hasInternalTransfer", internalTransferRecord != null);

                // Getting the LastBlockNumber from the Parser State
                result.put("lastBlockNumber", parserState.get().getBlockNumber());

                // Getting Token Information if Entered and Found
                if (tokenAddress != null) {
                    if(tokenAddress.startsWith("0x")) tokenAddress = tokenAddress.replace("0x", "");
                    TokenHolders tokenHolders = tokenHoldersService.findByContractAddrAndHolderAddr(tokenAddress, accountAddress);
                    if (tokenHolders != null) {
                        result.put(BALANCE, tokenHolders.getRawBalance());
                        Token token = tokenService.findByContractAddr(tokenAddress);
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

    @Override
    @Cacheable(CacheConfig.ACCOUNT_RICH_LIST)
    public String getAccountRichList() {
        JSONObject accountObj = new JSONObject();
        JSONArray accountArr = new JSONArray();

        Page<Account> accountPage = accountJpaRepository.getRichList(PageRequest.of(0, 25, new Sort(Sort.Direction.DESC, BALANCE)));
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
        List<TokenHolders> tokenHoldersList = tokenHoldersService.findByHolderAddr(holderAddress);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        JSONArray tokenArray = new JSONArray();
        if (tokenHoldersList != null && !tokenHoldersList.isEmpty()) {
            for (TokenHolders tokenHolders : tokenHoldersList) {
                Token token = tokenService.findByContractAddr(tokenHolders.getContractAddr());
                if (token != null) {
                    JSONObject result = new JSONObject(ow.writeValueAsString(token));
                    tokenArray.put(result);
                }
            }
        }

        return tokenArray.toString();
    }
    @Override
    public Account findByAddress(String address){
        return accountJpaRepository.findByAddress(address);
}

    @Override
    public Page<Account> getRichList(Pageable pageable){
        return accountJpaRepository.getRichList(pageable);
    }
}
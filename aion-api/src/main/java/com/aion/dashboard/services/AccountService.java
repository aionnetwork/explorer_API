package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.ParserState;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenHolders;
import com.aion.dashboard.repositories.*;
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

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


public interface AccountService {

    public String getAccountDetails(String accountAddress,
                                    String tokenAddress) throws Exception;
    public String getAccountRichList() ;
    public Account findByAddress(String address);
    Page<Account> getRichList(Pageable pageable);


}
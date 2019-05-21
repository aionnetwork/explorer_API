package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Token;
import com.aion.dashboard.entities.TokenHolders;
import com.aion.dashboard.entities.TokenTransfers;
import com.aion.dashboard.repositories.TokenHoldersJpaRepository;
import com.aion.dashboard.repositories.TokenJpaRepository;
import com.aion.dashboard.repositories.TokenTransfersJpaRepository;
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

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static java.lang.Math.min;


public interface TokenService {

    @Cacheable(CacheConfig.TOKEN_LIST)
    public String getTokenList(long start,
                               long end,
                               int pageNumber,
                               int pageSize) throws Exception;

    @Cacheable(CacheConfig.TOKEN_LIST_BY_TOKEN_NAME)
    String getTokenListByTokenName(String tokenName,
                                   int pageNumber,
                                   int pageSize) throws Exception;

    @Cacheable(CacheConfig.TOKEN_LIST_BY_TOKEN_SYMBOL)
    String getTokenListByTokenSymbol(String tokenSymbol,
                                     int pageNumber,
                                     int pageSize) throws Exception;

    @Cacheable(CacheConfig.TOKEN_DETAILS_TRANSFERS_AND_HOLDERS_BY_CONTRACT_ADDRESS)
    String getTokenDetailsTransfersAndHoldersByContractAddress(long start,
                                                               long end,
                                                               int holderPageNumber,
                                                               int holderPageSize,
                                                               int transferPageNumber,
                                                               int transferPageSize,
                                                               String contractAddr) throws Exception;

    Page<Token> findByYearAndMonthBetweenAndCreationTimestampBetween(int year, int startMonth, int endMonth, long start, long end, Pageable pageable);

    Page<Token> findByYearBetweenAndCreationTimestampBetween(int startYear, int endYear, long start, long end, Pageable pageable);

    Page<Token> findBySymbol(String symbol, Pageable pageable);

    Page<Token> findByName(String name, Pageable pageable);

    Token findByContractAddr(String contractAddr);

    List<Token> findAllByNameOrSymbol(String name, String symbol);

    boolean existsByContractAddr(String contractAddr);
}
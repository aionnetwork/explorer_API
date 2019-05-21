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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;


public interface ContractService {


    @Cacheable(CacheConfig.CONTRACT_LIST)
    String getContractList(long start,
                           long end,
                           int pageNumber,
                           int pageSize) throws Exception;

    @Cacheable(CacheConfig.CONTRACT_DETAIL_BY_CONTRACT_ADDRESS)
    String getContractDetailsByContractAddress(String searchParam,
                                               int eventPageNumber,
                                               int eventPageSize,
                                               int transactionPageNumber,
                                               int transactionPageSize) throws Exception;

    Contract findByContractAddr(String contractAddr);
    boolean existsByContractAddr(String addr);

    Page<Contract> findByYearAndMonthBetweenAndDeployTimestampBetween(int year,int startMonth, int endMonth, long start, long end, Pageable pageable);
    Page<Contract> findByYearBetweenAndDeployTimestampBetween(int startYear, int endYear, long start, long end, Pageable pageable);

}
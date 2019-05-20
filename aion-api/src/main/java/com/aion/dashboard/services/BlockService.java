package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.Account;
import com.aion.dashboard.entities.Block;
import com.aion.dashboard.entities.Transaction;
import com.aion.dashboard.repositories.AccountJpaRepository;
import com.aion.dashboard.repositories.BlockJpaRepository;
import com.aion.dashboard.repositories.ParserStateJpaRepository;
import com.aion.dashboard.repositories.TransactionJpaRepository;
import com.aion.dashboard.specification.BlockSpec;
import com.aion.dashboard.types.ParserStateType;
import com.aion.dashboard.utility.RewardsCalculator;
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
import java.time.ZonedDateTime;
import java.util.List;

import static com.aion.dashboard.utility.Logging.traceLogStartAndEnd;


public interface BlockService {
	@Cacheable(CacheConfig.BLOCK_LIST)
	String getBlockList(long start,
							   long end,
							   int pageNumber,
							   int pageSize) throws Exception;

	@Cacheable(CacheConfig.BLOCKS_MINED_BY_ADDRESS)
	String getBlocksMinedByAddress(long start,
								   long end,
								   int pageNumber,
								   int pageSize,
								   String accountAddress) throws Exception;

	@Cacheable(CacheConfig.BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER)
	String getBlockAndTransactionDetailsFromBlockNumberOrBlockHash(String searchParam) throws Exception;

	String getBlockNumber();
}
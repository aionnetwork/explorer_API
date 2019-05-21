package com.aion.dashboard.services;

import com.aion.dashboard.configs.CacheConfig;
import com.aion.dashboard.entities.*;
import com.aion.dashboard.repositories.*;
import com.aion.dashboard.specification.TransactionSpec;
import com.aion.dashboard.specification.TransferSpec;
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
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


public interface TransactionService {

	@Cacheable(CacheConfig.TRANSACTION_DETAIL_FROM_TRANSACTION_HASH)
	String getTransactionDetailsByTransactionHash(String searchParam,
												  int pageNumber,
												  int pageSize) throws Exception;

	@Cacheable(CacheConfig.TRANSACTION_LIST)
	String getTransactionList(int pageNumber,
							  int pageSize,
							  long startTime,
							  long endTime) throws Exception;

	@Cacheable(CacheConfig.TRANSACTIONS_BY_ADDRESS_FOR_TOKEN)
	String getTransactionsByAddressForToken(int pageNumber,
											int pageSize,
											long timeStampStart,
											long timeStampEnd,
											String address,
											String tokenAddress) throws Exception;

	@Cacheable(CacheConfig.TRANSACTIONS_BY_ADDRESS_FOR_AION)
	String getTransactionsByAddressForNative(int pageNumber,
											 int pageSize,
											 long timeStampStart,
											 long timeStampEnd,
											 String address) throws Exception;

	String getInternalTransfersByAddress(int pageNumber,
										 int pageSize,
										 long timeStampStart,
										 long timeStampEnd,
										 String address) throws Exception;

	String getInternalTransfersByTransactionHash(int pageNumber,
												 int pageSize,
												 String transactionHash) throws Exception;

	List<Object> findAvgsAndCountForToAddressByTimestampRange(long start, long end, int yearStart, int monthStart, int yearEnd, int monthEnd);

	List<Object> findAvgsAndCountForFromAddressByTimestampRange(long start, long end, int yearStart, int monthStart, int yearEnd, int monthEnd);

	Page<Transaction> findTransactionsByAddress(String address, Pageable pageable);

	Page<Transaction> findByDayAndMonthAndYear(int day, int month, int year, Pageable pageable);

	Transaction findByTransactionHash(String transactionHash);

}
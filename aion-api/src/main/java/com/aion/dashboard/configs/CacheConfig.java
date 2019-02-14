package com.aion.dashboard.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

   @Autowired
   private Environment environment;

   private static final Integer DURATION = 10;
   private static final Integer MAX_SIZE = 1000000;

   // Tokens
   public static final String TOKEN_LIST = "tokenList";
   public static final String TOKEN_HOLDERS_TOTAL = "tokenHoldersTotal";
   public static final String TOKEN_TRANSFERS_TOTAL = "tokenTransfersTotal";
   public static final String TOKEN_HOLDERS_BY_CONTRACT_ADDRESS = "tokenHoldersByContractAddress";
   public static final String TOKEN_TRANSFERS_BY_CONTRACT_ADDRESS = "tokenTransfersByContractAddress";
   public static final String TOKEN_LIST_BY_TOKEN_NAME = "tokenListByTokenName";
   public static final String TOKEN_LIST_BY_TOKEN_SYMBOL = "tokenListByTokenSymbol";
   public static final String TOKEN_DETAILS_TRANSFERS_AND_HOLDERS_BY_CONTRACT_ADDRESS = "tokenAndTransactionAndAccountDetailFromTokenNameOrTokenAddress";

   // blocks
   public static final String BLOCK_LIST = "blockList";
   public static final String BLOCKS_MINED_BY_ADDRESS = "blocksMinedByAddress";
   public static final String BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER = "blockAndTransactionDetailFromBlockHashOrBlockNumber";

   // Contracts
   public static final String CONTRACT_LIST = "contractList";
   public static final String CONTRACT_DETAIL_BY_CONTRACT_ADDRESS = "contractDetailByContractAddress";
   public static final String CONTRACT_EVENTS_BY_CONTRACT_ADDRESS = "contractEventsByContractAddress";
   public static final String CONTRACT_TRANSACTIONS_BY_CONTRACT_ADDRESS = "contractTransactionsByContractAddress";

   // transactions
   public static final String TRANSACTION_LIST = "transactionList";
   public static final String TRANSACTIONS_BY_ADDRESS = "transactionsByAddress";
   public static final String TRANSACTIONS_BY_ADDRESS_FOR_AION = "transactionsByAddressForAion";
   public static final String TRANSACTIONS_BY_ADDRESS_FOR_TOKEN = "transactionsByAddressForToken";
   public static final String TRANSACTION_EVENTS_BY_TRANSACTION_ID = "transactionEventsByTransactionID";
   public static final String TRANSACTION_DETAIL_FROM_TRANSACTION_HASH = "transactionDetailFromTransactionHash";

   // Accounts
   public static final String ACCOUNT_DETAILS = "accountDetails";
   public static final String ACCOUNT_RICH_LIST = "accountRichList";
   public static final String ACCOUNT_TOKEN_LIST = "accountTokenList";

   // Statistics
   public static final String STATISTICS_SB_METRICS = "statisticsStableMetrics";
   public static final String STATISTICS_RT_METRICS = "statisticsRealtimeMetrics";
   public static final String STATISTICS_BLOCKS = "statisticsStableBlocks";
   public static final String STATISTICS_MINED_BLOCKS = "statisticsMinedBlocks";
   public static final String STATISTICS_TRANSACTIONS = "statisticsStableTransactions";
   public static final String STATISTICS_INBOUND_TRANSACTIONS = "statisticsMinedBlocks";
   public static final String STATISTICS_OUTBOUND_TRANSACTIONS = "statisticsMinedBlocks";

   // Miscellaneous
   public static final String SEND_FEEDBACK = "sendFeedback";
   public static final String GRAPHING_INFO_BY_TIMESTAMP = "graphingInfoByTimstamp";

   // Third Party API
   public static final String COUNT_OPERATIONS = "countOperations";
   public static final String CIRCULATING_SUPPLY = "circulatingSupply";

   @Bean
   public CacheManager cacheManager() {
      SimpleCacheManager cacheManager = new SimpleCacheManager();

      boolean cacheEnable = false;
      String datadogEnable = environment.getProperty("DATADOG_ENABLE");
      if(datadogEnable != null && datadogEnable.equalsIgnoreCase("true")) cacheEnable = true;

      cacheManager.setCaches(Arrays.asList(
              // Tokens
              buildExpireAfterWriteCache(TOKEN_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_HOLDERS_TOTAL, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_TRANSFERS_TOTAL, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_HOLDERS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_TRANSFERS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_LIST_BY_TOKEN_NAME, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_LIST_BY_TOKEN_SYMBOL, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_DETAILS_TRANSFERS_AND_HOLDERS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // blocks
              buildExpireAfterWriteCache(BLOCK_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(BLOCKS_MINED_BY_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Contracts
              buildExpireAfterWriteCache(CONTRACT_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_DETAIL_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_EVENTS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_TRANSACTIONS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // transactions
              buildExpireAfterWriteCache(TRANSACTION_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TRANSACTIONS_BY_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TRANSACTIONS_BY_ADDRESS_FOR_AION, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TRANSACTIONS_BY_ADDRESS_FOR_TOKEN, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TRANSACTION_EVENTS_BY_TRANSACTION_ID, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TRANSACTION_DETAIL_FROM_TRANSACTION_HASH, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              // Accounts
              buildExpireAfterWriteCache(ACCOUNT_DETAILS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(ACCOUNT_RICH_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(ACCOUNT_TOKEN_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Statistics
              buildExpireAfterWriteCache(STATISTICS_BLOCKS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_SB_METRICS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_RT_METRICS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_TRANSACTIONS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_MINED_BLOCKS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_INBOUND_TRANSACTIONS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_OUTBOUND_TRANSACTIONS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Miscellaneous
              buildExpireAfterWriteCache(SEND_FEEDBACK, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(GRAPHING_INFO_BY_TIMESTAMP, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Third Party API
              buildExpireAfterWriteCache(COUNT_OPERATIONS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CIRCULATING_SUPPLY, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable)
      ));
      return cacheManager;
   }

   private Cache buildExpireAfterWriteCache(String name, long duration, TimeUnit timeUnit, long maxSize, boolean cacheEnable) {
      Caffeine cc = Caffeine.newBuilder().expireAfterWrite(duration, timeUnit).maximumSize(maxSize);
      if(cacheEnable) cc.recordStats();
      return new CaffeineCache(name, cc.build());
   }
}

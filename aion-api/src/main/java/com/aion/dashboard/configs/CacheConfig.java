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
   private final  Integer DURATION = 10;
   private final Integer MAX_SIZE = 1000;
    @Autowired
   private Environment environment;

   // Tokens
   public final static String TOKEN_LIST = "tokenList";
   public final static String TOKEN_HOLDERS_TOTAL = "tokenHoldersTotal";
   public final static String TOKEN_TRANSFERS_TOTAL = "tokenTransfersTotal";
   public final static String TOKEN_HOLDERS_BY_CONTRACT_ADDRESS = "tokenHoldersByContractAddress";
   public final static String TOKEN_TRANSFERS_BY_CONTRACT_ADDRESS = "tokenTransfersByContractAddress";
   public final static String TOKEN_LIST_BY_TOKEN_NAME_OR_TOKEN_SYMBOL = "tokenListBytokenNameOrSymbol";
   public final static String TOKEN_DETAILS_TRANSFERS_AND_HOLDERS_BY_CONTRACT_ADDRESS = "tokenAndTransactionAndAccountDetailFromTokenNameOrTokenAddress";

   // Blocks
   public final static String BLOCK_LIST = "blockList";
   public final static String BLOCKS_MINED_BY_ADDRESS = "blocksMinedByAddress";
   public final static String BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER = "blockAndTransactionDetailFromBlockHashOrBlockNumber";

   // Contracts
   public final static String CONTRACT_LIST = "contractList";
   public final static String CONTRACT_DETAIL_BY_CONTRACT_ADDRESS = "contractDetailByContractAddress";
   public final static String CONTRACT_EVENTS_BY_CONTRACT_ADDRESS = "contractEventsByContractAddress";
   public final static String CONTRACT_TRANSACTIONS_BY_CONTRACT_ADDRESS = "contractTransactionsByContractAddress";

   // Transactions
   public final static String TRANSACTION_LIST = "transactionList";
   public final static String TRANSACTIONS_BY_ADDRESS = "transactionsByAddress";
   public final static String TRANSACTIONS_BY_ADDRESS_FOR_AION = "transactionsByAddressForAion";
   public final static String TRANSACTIONS_BY_ADDRESS_FOR_TOKEN = "transactionsByAddressForToken";
   public final static String TRANSACTION_EVENTS_BY_TRANSACTION_ID = "transactionEventsByTransactionID";
   public final static String TRANSACTION_DETAIL_FROM_TRANSACTION_HASH = "transactionDetailFromTransactionHash";

   // Accounts
   public final static String ACCOUNT_DETAILS = "accountDetails";
   public final static String ACCOUNT_RICH_LIST = "accountRichList";
   public final static String ACCOUNT_TOKEN_LIST = "accountTokenList";

/* CSV */
   // Accounts
   public static final String ACCOUNT_TXNS_TO_CSV = "accountTxnsToCsv";
   public static final String ACCOUNT_BLKS_TO_CSV = "accountBlksToCsv";
   public static final String ACCOUNT_TKNS_TO_CSV = "accountTknsToCsv";
   public static final String ACCOUNT_DETAILS_TO_CSV = "accountDetailsToCsv";

   // Blocks
   public static final String BLOCK_TXNS_TO_CSV = "blockTxnsToCsv";
   public static final String BLOCK_DETAILS_TO_CSV = "blockDetailsToCsv";

   // Tokens
   public static final String TOKEN_TXNS_TO_CSV = "tokenTxnsToCsv";
   public static final String TOKEN_HDRS_TO_CSV = "tokenHdrsToCsv";
   public static final String TOKEN_DETAILS_TO_CSV = "tokenDetailsToCsv";

   // Contracts
   public static final String CONTRACT_TXNS_TO_CSV = "contractTxnsToCsv";
   public static final String CONTRACT_EVTS_TO_CSV = "contractEvtsToCsv";
   public static final String CONTRACT_DETAILS_TO_CSV = "contractDetailsToCsv";

   // Transactions
   public static final String TRANSACTION_DETAILS_TO_CSV = "transactionDetailsToCsv";
/* CSV */

   // Statistics
   public static final String STATISTICS_RT_METRICS = "statisticsRealtimeMetrics";
   public static final String STATISTICS_SB_METRICS = "statisticsStableMetrics";
   public static final String STATISTICS_BLOCKS = "statisticsStableMBlocks";
   public static final String STATISTICS_TRANSACTIONS = "statisticsStableTransactions";

   // Miscellaneous
   public static final String SEND_FEEDBACK = "sendFeedback";
   public static final String GRAPHING_LATEST_TIMESTAMP = "graphingLatestTimestamp";
   public static final String GRAPHING_INFO_BY_TIMESTAMP = "graphingInfoByTimstamp";

   // Third Party API
   public final static String COUNT_OPERATIONS = "countOperations";
   public final static String CIRCULATING_SUPPLY = "circulatingSupply";

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
              buildExpireAfterWriteCache(TOKEN_LIST_BY_TOKEN_NAME_OR_TOKEN_SYMBOL, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_DETAILS_TRANSFERS_AND_HOLDERS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Blocks
              buildExpireAfterWriteCache(BLOCK_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(BLOCKS_MINED_BY_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(BLOCK_AND_TRANSACTION_DETAIL_FROM_BLOCK_HASH_OR_BLOCK_NUMBER, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Contracts
              buildExpireAfterWriteCache(CONTRACT_LIST, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_DETAIL_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_EVENTS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_TRANSACTIONS_BY_CONTRACT_ADDRESS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Transactions
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

      /* CSV */
              // Accounts
              buildExpireAfterWriteCache(ACCOUNT_TXNS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(ACCOUNT_BLKS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(ACCOUNT_TKNS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(ACCOUNT_DETAILS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Blocks
              buildExpireAfterWriteCache(BLOCK_TXNS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(BLOCK_DETAILS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Tokens
              buildExpireAfterWriteCache(TOKEN_TXNS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_HDRS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(TOKEN_DETAILS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Contracts
              buildExpireAfterWriteCache(CONTRACT_TXNS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_EVTS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(CONTRACT_DETAILS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Transactions
              buildExpireAfterWriteCache(TRANSACTION_DETAILS_TO_CSV, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
      /* CSV */

              // Statistics
              buildExpireAfterWriteCache(STATISTICS_RT_METRICS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_SB_METRICS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_BLOCKS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(STATISTICS_TRANSACTIONS, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),

              // Miscellaneous
              buildExpireAfterWriteCache(SEND_FEEDBACK, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
              buildExpireAfterWriteCache(GRAPHING_LATEST_TIMESTAMP, DURATION, TimeUnit.SECONDS, MAX_SIZE, cacheEnable),
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

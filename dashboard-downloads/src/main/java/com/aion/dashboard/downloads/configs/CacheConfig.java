package com.aion.dashboard.downloads.configs;

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

/* CSV */
    // Accounts
    public static final String ACCOUNT_BLKS_TO_CSV = "accountBlksToCsv";
    public static final String ACCOUNT_TKNS_TO_CSV = "accountTknsToCsv";
    public static final String ACCOUNT_TXNS_TO_CSV = "accountTxnsToCsv";
    public static final String ACCOUNT_ITXFS_TO_CSV = "accountITxfsToCsv";
    public static final String ACCOUNT_DETAILS_TO_CSV = "accountDetailsToCsv";

    // Blocks
    public static final String BLOCK_TXNS_TO_CSV = "blockTxnsToCsv";
    public static final String BLOCK_DETAILS_TO_CSV = "blockDetailsToCsv";

    // Tokens
    public static final String TOKEN_TXFS_TO_CSV = "tokenTxfsToCsv";
    public static final String TOKEN_HDRS_TO_CSV = "tokenHdrsToCsv";
    public static final String TOKEN_DETAILS_TO_CSV = "tokenDetailsToCsv";

    // Contracts
    public static final String CONTRACT_TXNS_TO_CSV = "contractTxnsToCsv";
    public static final String CONTRACT_EVTS_TO_CSV = "contractEvtsToCsv";
    public static final String CONTRACT_DETAILS_TO_CSV = "contractDetailsToCsv";

    // Transactions
    public static final String TRANSACTION_DETAILS_TO_CSV = "transactionDetailsToCsv";
/* CSV */

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        boolean cacheEnable = false;
        String datadogEnable = environment.getProperty("DATADOG_ENABLE");
        if(datadogEnable != null && datadogEnable.equalsIgnoreCase("true")) cacheEnable = true;

        cacheManager.setCaches(Arrays.asList(
            /* CSV */
                // Accounts
                buildExpireAfterWriteCache(ACCOUNT_BLKS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(ACCOUNT_TKNS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(ACCOUNT_TXNS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(ACCOUNT_ITXFS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(ACCOUNT_DETAILS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),

                // Blocks
                buildExpireAfterWriteCache(BLOCK_TXNS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(BLOCK_DETAILS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),

                // Tokens
                buildExpireAfterWriteCache(TOKEN_TXFS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(TOKEN_HDRS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(TOKEN_DETAILS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),

                // Contracts
                buildExpireAfterWriteCache(CONTRACT_TXNS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(CONTRACT_EVTS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),
                buildExpireAfterWriteCache(CONTRACT_DETAILS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable),

                // Transactions
                buildExpireAfterWriteCache(TRANSACTION_DETAILS_TO_CSV, 10, TimeUnit.SECONDS, 1000, cacheEnable)
            /* CSV */
        ));
        return cacheManager;
    }

    private Cache buildExpireAfterWriteCache(String name, long duration, TimeUnit timeUnit, long timestampEndSize, boolean cacheEnable) {
        Caffeine cc = Caffeine.newBuilder().expireAfterWrite(duration, timeUnit).maximumSize(timestampEndSize);
        if(cacheEnable) cc.recordStats();
        return new CaffeineCache(name, cc.build());
    }
}


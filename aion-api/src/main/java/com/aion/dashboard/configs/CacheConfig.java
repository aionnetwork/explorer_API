package com.aion.dashboard.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.github.benmanes.caffeine.cache.stats.StatsCounter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@EnableCaching
public class CacheConfig {
    // Tokens
    public static final String TOKENS = "TOKENS";
    public static final String TOKEN_LIST_BY_NAME = "TOKEN_LIST_BY_NAME";
    public static final String TOKEN_TRANSFERS = "TOKEN_TRANSFERS";
    // blocks
    public static final String BLOCK_LIST = "BLOCK_LIST";
    public static final String BLOCKS_MINED = "BLOCKS_MINED";
    public static final String BLOCKS_AND_TRANSACTION = "BLOCKS_AND_TRANSACTION_DETAILS";
    public static final String BLOCK = "block";
    public static final String BEST_BLOCK = "bestBlock";
    public static final String BLOCK_LIST_2 = "block_list_2";
    public static final String BLOCK_LIST_3 = "block_list_3";
    // Contracts
    public static final String CONTRACT_LIST = "CONTRACT_LIST";
    public static final String CONTRACT_DETAILS = "CONTRACT_DETAILS";
    // transactions
    public static final String TRANSACTIONS = "transactions";
    public static final String TRANSACTIONS_2 = "transactions_paging";
    public static final String TRANSACTION_LIST = "TRANSACTION_LIST";
    public static final String TRANSACTION_BY_ADDRESS = "TRANSACTIONS_BY_ADDRESS";
    public static final String TRANSACTION = "TRANSACTION";
    // Accounts
    public static final String ACCOUNT_LIST = "accountList";
    public static final String RICH_LIST = "RICH_LIST";
    public static final String ACCOUNT_DETAILS = "ACCOUNT_DETAILS";
    // Statistics
    public static final String STATISTICS_METRICS = "statisticsMetrics";
    public static final String STATISTICS_VALIDATORS = "Validators";
    public static final String STATISTICS_ACCOUNT_STATS = "ACCOUNT_STATISTICS";
    // Miscellaneous
    public static final String GRAPHING_INFO_BY_TIMESTAMP = "graphingInfoByTimstamp";
    public static final String VIEW_V2 = "viewV2";
    public static final String VIEW_V1 = "viewV1";
    public static final String SEARCH = "SEARCH";
    // Third Party API
    public static final String CIRCULATING_SUPPLY = "circulatingSupply";
    public static final String BLOCK_NUMBER = "BlockNumber";
    public static final String BLOCK_NUMBER_2 = "BlockNumber2";
    public static final String BLOCK_NUMBER_3 = "BlockNumber3";
    // TX LOG
    public static final String TX_LOG = "txlog";
    // INTERNAL TX
    public static final String INTERNAL_TRANSACTION = "internal_transaction";
    //Cache sizes
    private static final Integer LIST_SIZE = 50;
    private static final Integer MAX_SIZE = 100;
    private static final Integer MIN_SIZE = 1;
    //Cache durations
    private static final Integer BLOCK_INTERVAL_DURATION = 10;
    private static final Integer MINUTE_DURATION = 60;
    private static final Integer CONSTANT_VALUE_DURATION = 300;

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);
    private final Executor cacheExecutor = Executors.newSingleThreadExecutor();
    @Autowired Environment environment;
    @Value("${com.aion.dashboard.configs.CacheConfig.cacheEnable}")
    private boolean cacheEnable;
    @Value("${management.metrics.export.datadog.enabled}")
    private boolean datadogEnable;
    private boolean testingEnable;

    @Bean
    public CacheManager cacheManager() {
        testingEnable = environment.getProperty("testing", "false").equalsIgnoreCase("true");
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(buildAllCaches());
        return cacheManager;
    }

    private List<Cache> buildAllCaches() {
        return List.of(
                // Tokens
                buildExpireAfterWriteCache(TOKEN_TRANSFERS, BLOCK_INTERVAL_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(TOKEN_LIST_BY_NAME, MINUTE_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(TOKENS, MINUTE_DURATION, MAX_SIZE),
                // blocks
                buildExpireAfterWriteCache(BLOCK, MINUTE_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(BEST_BLOCK, BLOCK_INTERVAL_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(BLOCK_LIST_2, BLOCK_INTERVAL_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(BLOCK_LIST_3, MINUTE_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(BLOCKS_AND_TRANSACTION, MINUTE_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(BLOCKS_MINED, MINUTE_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(BLOCK_LIST, BLOCK_INTERVAL_DURATION, LIST_SIZE),
                // Contracts
                buildExpireAfterWriteCache(CONTRACT_LIST, MINUTE_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(CONTRACT_DETAILS, BLOCK_INTERVAL_DURATION, LIST_SIZE),
                // transactions
                buildExpireAfterWriteCache(TRANSACTION_LIST, BLOCK_INTERVAL_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(
                        TRANSACTION_BY_ADDRESS, BLOCK_INTERVAL_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(TRANSACTION, CONSTANT_VALUE_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(TRANSACTIONS, CONSTANT_VALUE_DURATION, LIST_SIZE),
                buildExpireAfterWriteCache(TRANSACTIONS_2, BLOCK_INTERVAL_DURATION, LIST_SIZE),
                // Accounts
                buildExpireAfterWriteCache(ACCOUNT_LIST, BLOCK_INTERVAL_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(RICH_LIST, CONSTANT_VALUE_DURATION, MIN_SIZE),
                buildExpireAfterWriteCache(ACCOUNT_DETAILS, BLOCK_INTERVAL_DURATION, MAX_SIZE),

                // Statistics
                buildExpireAfterWriteCache(
                        STATISTICS_VALIDATORS, MINUTE_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(STATISTICS_METRICS, BLOCK_INTERVAL_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(
                        STATISTICS_ACCOUNT_STATS, BLOCK_INTERVAL_DURATION, MIN_SIZE),
                // Miscellaneous
                buildExpireAfterWriteCache(GRAPHING_INFO_BY_TIMESTAMP, MINUTE_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(VIEW_V2, BLOCK_INTERVAL_DURATION, MIN_SIZE),
                buildExpireAfterWriteCache(VIEW_V1, BLOCK_INTERVAL_DURATION, MIN_SIZE),
                buildExpireAfterWriteCache(SEARCH, CONSTANT_VALUE_DURATION, MAX_SIZE),
                // Third Party API
                buildExpireAfterWriteCache(CIRCULATING_SUPPLY, CONSTANT_VALUE_DURATION, MAX_SIZE),
                buildExpireAfterWriteCache(BLOCK_NUMBER, BLOCK_INTERVAL_DURATION, MIN_SIZE),
                buildExpireAfterWriteCache(BLOCK_NUMBER_2, BLOCK_INTERVAL_DURATION, MIN_SIZE),
                buildExpireAfterWriteCache(BLOCK_NUMBER_3, BLOCK_INTERVAL_DURATION, MIN_SIZE),

                // Tx log
                buildExpireAfterWriteCache(TX_LOG, MINUTE_DURATION, MAX_SIZE),
                // internal transaction
                buildExpireAfterWriteCache(INTERNAL_TRANSACTION, MINUTE_DURATION, MAX_SIZE));
    }

    private Cache buildExpireAfterWriteCache(final String name, long duration, long maxSize) {
        if (LOGGER.isInfoEnabled() && cacheEnable) {
            LOGGER.info("Enabling cache: {}", name);
        } else if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Cache {} is disabled.", name);
        }
        Caffeine cc =
                Caffeine.newBuilder()
                        .expireAfterWrite(duration, TimeUnit.SECONDS)
                        .initialCapacity(0)
                        .maximumSize(cacheEnable ? maxSize : 0)
                        .removalListener(
                                (key, value, cause) -> {
                                    if (LOGGER.isTraceEnabled()) {
                                        LOGGER.trace("Evicting value from: {}", name);
                                    }
                                })
                        .executor(cacheExecutor);

        if (testingEnable) cc.recordStats(() -> new MockedStatsCounter(name));
        else if (datadogEnable) cc.recordStats();
        //noinspection unchecked
        return new CaffeineCache(name, cc.build());
    }

    // Mocked counter to be used for testing
    public static class MockedStatsCounter implements StatsCounter {
        private static final Map<String, MockedStatsCounter> instanceMap =
                Collections.synchronizedMap(new HashMap<>());
        private static final Logger STATS_COUNTER_LOGGER = LoggerFactory.getLogger(StatsCounter.class);
        private final String name;
        private LongAccumulator hitCounter = simpleSummingAccumulator();
        private LongAccumulator missCounter = simpleSummingAccumulator();
        private LongAccumulator recordLoadSuccess = simpleSummingAccumulator();
        private LongAccumulator recordLoadFailure = simpleSummingAccumulator();
        private LongAccumulator recordEviction = simpleSummingAccumulator();
        private LongAccumulator totalLoadCount = simpleSummingAccumulator();
        private LongAccumulator evictionWeightCounter = simpleSummingAccumulator();
        private MockedStatsCounter(String name) {
            this.name = name;
            instanceMap.put(name, this);
        }

        public static MockedStatsCounter getStatsCounterFor(String name) {
            return instanceMap.get(name);
        }

        private static LongAccumulator simpleSummingAccumulator() {
            return new LongAccumulator(Long::sum, 0);
        }

        @Override
        public void recordHits(int count) {
            STATS_COUNTER_LOGGER.trace("Hit for {}", name);
            hitCounter.accumulate(count);
        }

        @Override
        public void recordMisses(int count) {
            STATS_COUNTER_LOGGER.trace("Miss for {}", name);
            missCounter.accumulate(count);
        }

        @Override
        public void recordLoadSuccess(long loadTime) {
            recordLoadSuccess.accumulate(loadTime);
            totalLoadCount.accumulate(loadTime);
        }

        @Override
        public void recordLoadFailure(long loadTime) {
            recordLoadFailure.accumulate(loadTime);
            totalLoadCount.accumulate(loadTime);
        }

        @Override
        public void recordEviction() {
            this.recordEviction.accumulate(1);
        }

        @Override
        public void recordEviction(int weight) {
            evictionWeightCounter.accumulate(weight);
        }

        @Override
        public CacheStats snapshot() {
            return new CacheStats(
                    hitCounter.get(),
                    missCounter.get(),
                    recordLoadSuccess.get(),
                    recordLoadFailure.get(),
                    totalLoadCount.get(),
                    recordEviction.get(),
                    evictionWeightCounter.get());
        }
    }
}

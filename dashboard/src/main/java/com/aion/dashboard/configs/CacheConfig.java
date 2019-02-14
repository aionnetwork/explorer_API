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

    public static final String STORE_FEEDBACK = "sendFeedback";


    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        boolean cacheEnable = false;
        String datadogEnable = environment.getProperty("DATADOG_ENABLE");
        if(datadogEnable != null && datadogEnable.equalsIgnoreCase("true")) cacheEnable = true;

        cacheManager.setCaches(Arrays.asList(
                buildExpireAfterWriteCache(STORE_FEEDBACK, 10, TimeUnit.SECONDS, 1000, cacheEnable)
        ));
        return cacheManager;
    }

    private Cache buildExpireAfterWriteCache(String name, long duration, TimeUnit timeUnit, long maxSize, boolean cacheEnable) {
        Caffeine cc = Caffeine.newBuilder().expireAfterWrite(duration, timeUnit).maximumSize(maxSize);
        if(cacheEnable) cc.recordStats();
        return new CaffeineCache(name, cc.build());
    }
}


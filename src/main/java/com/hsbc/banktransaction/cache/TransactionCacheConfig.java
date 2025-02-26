package com.hsbc.banktransaction.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置类，启用缓存并配置缓存管理器
 */
@Configuration
@EnableCaching
public class TransactionCacheConfig {

    /**
     * 创建缓存管理器的 Bean
     * @return 缓存管理器实例
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("transactions");
    }
}
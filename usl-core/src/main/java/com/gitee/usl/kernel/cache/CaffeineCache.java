package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
@Order(CaffeineCache.USL_CACHE_ORDER)
@AutoService(Cache.class)
public class CaffeineCache implements Cache {
    /**
     * USL 缓存的优先级
     * 若想使用自定义缓存替代 USL 内置提供的 Caffeine 缓存
     * 或想使用多级缓存来替代 USL 内置的一级缓存
     * 则可以实现 UslCache 接口，默认优先级为 0
     */
    public static final int USL_CACHE_ORDER = Integer.MAX_VALUE - 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private com.github.benmanes.caffeine.cache.Cache<String, Expression> cache;

    @Override
    public void init(CacheConfiguration configuration) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(10)
                .recordStats()
                .build();
    }

    @Override
    public Expression select(String key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void remove(String key) {
        this.cache.invalidate(key);
    }

    @Override
    public void insert(String key, Expression expression) {
        this.cache.put(key, expression);
    }

    public com.github.benmanes.caffeine.cache.Cache<String, Expression> getCache() {
        return cache;
    }

    @Override
    public void snapshot() {
        CacheStats stats = this.cache.stats();

        logger.info("[USL Cache Snapshot] => [HC : {}, MC : {}, EC : {}]",
                stats.hitCount(),
                stats.missCount(),
                stats.evictionCount());
    }
}

package com.gitee.usl.kernel.cache;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.CacheConfig;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.auto.service.AutoService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
@Order(CaffeineCache.USL_CACHE_ORDER)
@AutoService(ExpressionCache.class)
public class CaffeineCache implements ExpressionCache {

    @Description("USL内置缓存默认实现类的优先级")
    public static final int USL_CACHE_ORDER = Integer.MAX_VALUE - 100;

    @Description("Caffeine缓存实例")
    private Cache<String, CacheValue> cache;

    @Override
    public void init(CacheConfig configuration) {
        this.cache = Caffeine.newBuilder()
                .softValues()
                .recordStats()
                .maximumSize(configuration.getMaxSize())
                .initialCapacity(configuration.getInitSize())
                .expireAfterAccess(configuration.getDuration())
                .build();

        log.info("缓存实例构建成功 - {}", configuration.snapshot());
    }

    @Override
    public CacheValue select(String key) {
        return this.cache.getIfPresent(key);
    }

    @Override
    public void remove(String key) {
        this.cache.invalidate(key);
        log.debug("剔除缓存 - [{}]", key);
    }

    @Override
    public void insert(String key, CacheValue cacheValue) {
        Assert.notNull(cacheValue, "缓存值不能为空");
        this.cache.put(key, cacheValue);
        log.debug("新增缓存 - [{}]", key);
    }

    @Override
    public void snapshot() {
        CacheStats stats = this.cache.stats();

        log.info("[USL缓存快照] - [命中数 : {}, 未命中数 : {}, 剔除数 : {}]",
                stats.hitCount(),
                stats.missCount(),
                stats.evictionCount());
    }
}

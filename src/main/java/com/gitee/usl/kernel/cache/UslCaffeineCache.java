package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.googlecode.aviator.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
@Order(UslCaffeineCache.USL_CACHE_ORDER)
public class UslCaffeineCache implements UslCache, Initializer {
    /**
     * USL 缓存的优先级
     * 若想使用自定义缓存替代 USL 内置提供的 Caffeine 缓存
     * 或想使用多级缓存来替代 USL 内置的一级缓存
     * 则可以实现 UslCache 接口，默认优先级为 0
     */
    public static final int USL_CACHE_ORDER = Integer.MAX_VALUE - 100;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Cache<String, Expression> cache;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        CacheConfiguration configuration = uslConfiguration.getCacheConfiguration();

        // 根据SPI机制加载优先级最高的 UslCache 接口实现类
        UslCache uslCache = SpiServiceUtil.firstService(UslCache.class);

        // 若 UslCache 为 UslCaffeineCache
        // 则根据缓存配置初始化 UslCaffeineCache 中的 Cache 成员变量
        if (uslCache instanceof UslCaffeineCache uslCaffeineCache) {
            uslCaffeineCache.cache = Caffeine.newBuilder()
                    .maximumSize(10)
                    .recordStats()
                    .build();
        }

        configuration.setUslCache(uslCache);
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

    public Cache<String, Expression> getCache() {
        return cache;
    }

    @Override
    public void snapshot() {
        CacheStats stats = this.cache.stats();
        if (stats == null) {
            return;
        }

        logger.info("[USL Cache Snapshot] => [HC : {}, MC : {}, EC : {}]",
                stats.hitCount(),
                stats.missCount(),
                stats.evictionCount());
    }
}

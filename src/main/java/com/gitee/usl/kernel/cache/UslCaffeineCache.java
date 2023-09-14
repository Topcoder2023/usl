package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
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
public class UslCaffeineCache implements UslCache, Initializer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Cache<String, Expression> cache;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        CacheConfiguration configuration = uslConfiguration.getCacheConfiguration();

        UslCache uslCache = SpiServiceUtil.loadFirstSortedService(UslCache.class);
        if (uslCache == null) {
            this.cache = Caffeine.newBuilder()
                    .maximumSize(10)
                    .recordStats()
                    .build();

            configuration.setUslCache(this);
            return;
        }

        configuration.setUslCache(uslCache);
    }

    @Override
    public void select(String key) {
        this.cache.getIfPresent(key);
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

        logger.info("USL Cache Snapshot => [HC : {}, MC : {}, EC : {}]",
                stats.hitCount(),
                stats.missCount(),
                stats.evictionCount());
    }
}

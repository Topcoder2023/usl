package com.gitee.usl.kernel.configure;

import com.gitee.usl.kernel.cache.CacheManager;

/**
 * @author hongda.li
 */
public class CacheConfiguration {
    private final UslConfiguration configuration;
    private CacheManager cacheManager;

    public CacheConfiguration(UslConfiguration configuration) {
        this.configuration = configuration;
    }

    public UslConfiguration finish() {
        return this.configuration;
    }

    public CacheManager cacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}

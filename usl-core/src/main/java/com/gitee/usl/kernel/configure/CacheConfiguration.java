package com.gitee.usl.kernel.configure;

import com.gitee.usl.kernel.cache.CacheManager;

/**
 * @author hongda.li
 */
public class CacheConfiguration {
    private final Configuration configuration;
    private CacheManager cacheManager;

    public CacheConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration finish() {
        return this.configuration;
    }

    public CacheManager cacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}

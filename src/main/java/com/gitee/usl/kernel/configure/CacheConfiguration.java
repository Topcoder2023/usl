package com.gitee.usl.kernel.configure;

import com.gitee.usl.kernel.cache.CacheManager;

/**
 * @author hongda.li
 */
public class CacheConfiguration {
    private CacheManager cacheManager;

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public CacheConfiguration setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
        return this;
    }
}

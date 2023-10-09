package com.gitee.usl.kernel.configure;

import com.gitee.usl.kernel.cache.CacheManager;

/**
 * @author hongda.li
 */
public class CacheConfiguration {
    private CacheManager cacheManager;

    public CacheManager cacheManager() {
        return cacheManager;
    }

    public void cacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}

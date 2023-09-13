package com.gitee.usl.kernel.configure;

import com.gitee.usl.kernel.cache.UslCache;

/**
 * @author hongda.li
 */
public class CacheConfiguration {
    private UslCache uslCache;

    public UslCache getUslCache() {
        return uslCache;
    }

    public CacheConfiguration setUslCache(UslCache uslCache) {
        this.uslCache = uslCache;
        return this;
    }
}

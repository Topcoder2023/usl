package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;

/**
 * @author hongda.li
 */
@Order
public class UslCacheManager implements Initializer {
    private final UslCache uslCache;

    public UslCacheManager() {
        uslCache = SpiServiceUtil.loadFirstSortedService(UslCache.class);
    }

    @Override
    public void doInit(UslConfiguration configuration) {

    }
}

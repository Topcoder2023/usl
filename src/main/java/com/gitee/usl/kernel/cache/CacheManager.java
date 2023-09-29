package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.utils.SpiServiceUtil;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(Initializer.class)
public class CacheManager implements Initializer {
    private UslCache uslCache;

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        UslCache found = SpiServiceUtil.firstService(UslCache.class);
        if (found == null) {
            return;
        }

        this.uslCache = found;

        CacheConfiguration configuration = uslConfiguration.getCacheConfiguration();
        this.uslCache.init(configuration);
        configuration.setCacheManager(this);
    }

    public UslCache getUslCache() {
        return uslCache;
    }
}

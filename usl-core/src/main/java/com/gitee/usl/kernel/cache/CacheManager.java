package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.CacheConfiguration;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(Initializer.class)
public class CacheManager implements Initializer {
    private Cache cache;

    @Override
    public void doInit(Configuration uslConfiguration) {
        Cache found = ServiceSearcher.searchFirst(Cache.class);
        if (found == null) {
            return;
        }

        this.cache = found;

        CacheConfiguration configuration = uslConfiguration.configCache();
        this.cache.init(configuration);
        configuration.setCacheManager(this);
    }

    public Cache cache() {
        return cache;
    }
}

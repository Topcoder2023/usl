package com.gitee.usl.infra.time.impl;

import com.gitee.usl.infra.time.FixedRateTimedTask;
import com.gitee.usl.infra.time.TimedTask;
import com.gitee.usl.kernel.cache.CacheManager;
import com.gitee.usl.kernel.cache.Cache;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.google.auto.service.AutoService;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@AutoService(TimedTask.class)
public class CacheStatusWatcher implements FixedRateTimedTask {
    @Override
    public long cycle() {
        return 30;
    }

    @Override
    public TimeUnit unit() {
        return TimeUnit.MINUTES;
    }

    @Override
    public long initDelay() {
        // 等USL-Cache加载后再执行
        return 5;
    }

    @Override
    public void doTask(UslConfiguration configuration) {
        Optional.ofNullable(configuration.getCacheConfiguration().cacheManager())
                .map(CacheManager::cache)
                .ifPresent(Cache::snapshot);
    }
}

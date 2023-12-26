package com.gitee.usl.kernel.cache;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.time.FixedRateTimedTask;
import com.gitee.usl.infra.time.TimedTask;
import com.gitee.usl.kernel.cache.CacheInitializer;
import com.gitee.usl.kernel.cache.ExpressionCache;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Description("缓存状态监控")
@AutoService(TimedTask.class)
public class CacheStatusWatcher implements FixedRateTimedTask {
    @Override
    public long cycle() {
        return 5;
    }

    @Override
    public TimeUnit unit() {
        return TimeUnit.MINUTES;
    }

    @Override
    public long initDelay() {
        return 1;
    }

    @Override
    public void doTask(Configuration configuration) {
        CacheInitializer initializer = configuration
                .getCacheConfig()
                .getCacheInitializer();

        if (initializer == null) {
            return;
        }

        ExpressionCache cache = initializer.getCache();
        cache.snapshot();
    }
}

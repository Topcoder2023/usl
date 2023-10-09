package com.gitee.usl.app.plugin;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.UslExecuteException;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * 限流插件
 *
 * @author hongda.li
 */
@SuppressWarnings("UnstableApiUsage")
public class RateLimitPlugin implements BeginPlugin {
    private final RateLimiter rateLimiter;
    private final long timeout;
    private final TimeUnit unit;

    public RateLimitPlugin(double qps, long timeout, TimeUnit unit) {
        this.rateLimiter = RateLimiter.create(qps);
        this.timeout = timeout;
        this.unit = unit;
    }

    @Override
    public void onBegin(FunctionSession session) {
        boolean acquired = rateLimiter.tryAcquire(this.timeout, this.unit);

        Assert.isTrue(acquired, () -> new UslExecuteException(ResultCode.TIMEOUT));
    }
}

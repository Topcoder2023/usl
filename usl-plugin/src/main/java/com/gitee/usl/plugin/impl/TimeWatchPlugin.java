package com.gitee.usl.plugin.impl;

import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.plugin.api.TimeWatchListener;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 执行时长监控插件
 *
 * @author hongda.li
 */
public class TimeWatchPlugin implements BeginPlugin, SuccessPlugin, FailurePlugin, FinallyPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeWatchPlugin.class);
    private final ThreadLocal<Long> start = new ThreadLocal<>();
    private final long threshold;
    private final TimeWatchListener listener;
    private final AtomicLong totalCount = new AtomicLong(NumberConstant.ZERO);
    private final AtomicLong successCount = new AtomicLong(NumberConstant.ZERO);
    private final AtomicLong failureCount = new AtomicLong(NumberConstant.ZERO);

    public TimeWatchPlugin(long threshold, TimeUnit unit, TimeWatchListener listener) {
        this.listener = listener;
        this.threshold = unit.toNanos(threshold);
    }

    @Override
    public void onBegin(FunctionSession session) {
        this.start.set(System.nanoTime());
    }

    @Override
    public void onFinally(FunctionSession session) {
        // 执行次数自增
        totalCount.incrementAndGet();

        // 执行的总时间
        long totalTime = System.nanoTime() - start.get();

        // 移除当前线程变量
        start.remove();

        // 触发监听策略
        if (totalTime >= threshold) {
            listener.onListen(session, totalTime);
        }
    }

    @Override
    public void onFailure(FunctionSession session) {
        // 执行失败次数自增
        failureCount.incrementAndGet();
    }

    @Override
    public void onSuccess(FunctionSession session) {
        // 执行成功次数自增
        successCount.incrementAndGet();
    }

    public long getTotalCount() {
        return totalCount.get();
    }

    public long getSuccessCount() {
        return successCount.get();
    }

    public long getFailureCount() {
        return failureCount.get();
    }

    public static final class DefaultTimeWatchListener implements TimeWatchListener {
        private final TimeUnit unit;

        public DefaultTimeWatchListener(TimeUnit unit) {
            this.unit = unit;
        }

        @Override
        public void onListen(FunctionSession session, long totalTime) {
            long converted = unit.convert(totalTime, TimeUnit.NANOSECONDS);
            LOGGER.warn("Function[{}] execute finished and cost time - [{}({})]",
                    session.definition().name(),
                    converted,
                    unit.name());
        }
    }
}

package com.gitee.usl.plugin.impl;

import com.gitee.usl.plugin.api.TimeWatchListener;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 执行时长监控插件
 *
 * @author hongda.li
 */
public class TimeWatchPlugin implements BeginPlugin, FinallyPlugin {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeWatchPlugin.class);
    private final ThreadLocal<Long> start = new ThreadLocal<>();
    private final long threshold;
    private final TimeWatchListener listener;

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
        // 执行的总时间
        long totalTime = System.nanoTime() - start.get();

        // 移除当前线程变量
        start.remove();

        // 触发监听策略
        if (totalTime >= threshold) {
            listener.onListen(session, totalTime);
        }
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

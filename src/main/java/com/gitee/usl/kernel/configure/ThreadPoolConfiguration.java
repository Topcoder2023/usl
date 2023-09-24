package com.gitee.usl.kernel.configure;

import com.gitee.usl.infra.thread.UslExecutorManager;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
public class ThreadPoolConfiguration {
    private int corePoolSize;
    private int maxPoolSize;
    private boolean allowedTimeout;
    private int queueSize;
    private int aliveTime;
    private TimeUnit timeUnit;
    private UslExecutorManager uslExecutorManager;

    public ThreadPoolConfiguration() {
        // 获取CPU核心数
        int processor = Runtime.getRuntime().availableProcessors();

        this.corePoolSize = processor;
        this.maxPoolSize = processor << 2;
        this.allowedTimeout = false;
        this.queueSize = processor << 3;
        this.aliveTime = 60;
        this.timeUnit = TimeUnit.SECONDS;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ThreadPoolConfiguration setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public ThreadPoolConfiguration setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public boolean isAllowedTimeout() {
        return allowedTimeout;
    }

    public ThreadPoolConfiguration setAllowedTimeout(boolean allowedTimeout) {
        this.allowedTimeout = allowedTimeout;
        return this;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public ThreadPoolConfiguration setQueueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public ThreadPoolConfiguration setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
        return this;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public ThreadPoolConfiguration setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public UslExecutorManager getUslExecutorManager() {
        return uslExecutorManager;
    }

    public ThreadPoolConfiguration setUslExecutorManager(UslExecutorManager uslExecutorManager) {
        this.uslExecutorManager = uslExecutorManager;
        return this;
    }

    @Override
    public String toString() {
        return "ThreadPoolConfiguration{" +
                "corePoolSize=" + corePoolSize +
                ", maxPoolSize=" + maxPoolSize +
                ", allowedTimeout=" + allowedTimeout +
                ", queueSize=" + queueSize +
                ", aliveTime=" + aliveTime +
                ", timeUnit=" + timeUnit +
                '}';
    }
}

package com.gitee.usl.kernel.configure;

import com.gitee.usl.infra.thread.ExecutorPoolManager;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
public class ExecutorConfiguration {
    private final UslConfiguration configuration;
    private int corePoolSize;
    private int maxPoolSize;
    private boolean allowedTimeout;
    private int queueSize;
    private int aliveTime;
    private TimeUnit timeUnit;
    private ExecutorPoolManager executorPoolManager;

    public ExecutorConfiguration(UslConfiguration configuration) {
        this.configuration = configuration;
    }

    public UslConfiguration finish() {
        return this.configuration;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ExecutorConfiguration setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public ExecutorConfiguration setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
        return this;
    }

    public boolean isAllowedTimeout() {
        return allowedTimeout;
    }

    public ExecutorConfiguration setAllowedTimeout(boolean allowedTimeout) {
        this.allowedTimeout = allowedTimeout;
        return this;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public ExecutorConfiguration setQueueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }

    public int getAliveTime() {
        return aliveTime;
    }

    public ExecutorConfiguration setAliveTime(int aliveTime) {
        this.aliveTime = aliveTime;
        return this;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public ExecutorConfiguration setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    public ExecutorPoolManager executorManager() {
        return executorPoolManager;
    }

    public ExecutorConfiguration setUslExecutorManager(ExecutorPoolManager executorPoolManager) {
        this.executorPoolManager = executorPoolManager;
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

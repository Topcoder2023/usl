package com.gitee.usl.kernel.configure;

import java.util.function.Consumer;

/**
 * USL 配置类
 *
 * @author hongda.li
 */
public final class UslConfiguration {
    private final CacheConfiguration cacheConfiguration;
    private final EngineConfiguration engineConfiguration;
    private final ThreadPoolConfiguration threadPoolConfiguration;

    public UslConfiguration() {
        this(new CacheConfiguration(), new EngineConfiguration(), new ThreadPoolConfiguration());
    }

    public UslConfiguration(CacheConfiguration cacheConfiguration,
                            EngineConfiguration engineConfiguration,
                            ThreadPoolConfiguration threadPoolConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
        this.engineConfiguration = engineConfiguration;
        this.threadPoolConfiguration = threadPoolConfiguration;
    }

    /**
     * 配置脚本引擎
     *
     * @param consumer 脚本引擎配置消费者
     * @return 链式调用
     */
    public UslConfiguration configEngine(Consumer<EngineConfiguration> consumer) {
        consumer.accept(engineConfiguration);
        return this;
    }

    /**
     * 配置线程池
     *
     * @param consumer 线程池配置消费者
     * @return 链式调用
     */
    public UslConfiguration configThreadPool(Consumer<ThreadPoolConfiguration> consumer) {
        consumer.accept(threadPoolConfiguration);
        return this;
    }

    public CacheConfiguration getCacheConfiguration() {
        return cacheConfiguration;
    }

    public EngineConfiguration getEngineConfiguration() {
        return engineConfiguration;
    }

    public ThreadPoolConfiguration getThreadPoolConfiguration() {
        return threadPoolConfiguration;
    }
}

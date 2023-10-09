package com.gitee.usl.kernel.configure;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * USL 配置类
 * USL 配置类由不同功能模块的配置类共同组成
 * 配置类通常不会直接暴露其底层实现
 * 每个具体的配置类持有当前功能模块的配置参数与管理者实例
 * 例如，QueueConfiguration 持有 CompileQueueManager 实例
 * CacheConfiguration 持有 UslCache 实例
 * 因此，可以通过 USL 配置类获取任意功能模块的管理者
 * 并通过功能模块的管理者进行模块之间的调度
 * 同时，为了更好的扩展其它未知参数，USL 预留了 customConfiguration 自定义的 Map 类型
 *
 * @author hongda.li
 */
public final class UslConfiguration {
    /**
     * 缓存配置类
     */
    private final CacheConfiguration cacheConfiguration;

    /**
     * 编译队列配置类
     */
    private final QueueConfiguration queueConfiguration;

    /**
     * 自定义扩展配置类
     */
    private final Map<String, Object> customConfiguration;

    /**
     * 脚本引擎配置类
     */
    private final EngineConfiguration engineConfiguration;

    /**
     * 线程池配置类
     */
    private final ThreadPoolConfiguration threadPoolConfiguration;

    public UslConfiguration() {
        this(new CacheConfiguration(),
                new QueueConfiguration(),
                HashMap.newHashMap(NumberConstant.COMMON_SIZE),
                new EngineConfiguration(),
                new ThreadPoolConfiguration());
    }

    public UslConfiguration(CacheConfiguration cacheConfiguration,
                            QueueConfiguration queueConfiguration,
                            Map<String, Object> customConfiguration,
                            EngineConfiguration engineConfiguration,
                            ThreadPoolConfiguration threadPoolConfiguration) {
        this.cacheConfiguration = cacheConfiguration;
        this.queueConfiguration = queueConfiguration;
        this.customConfiguration = customConfiguration;
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

    public QueueConfiguration queueConfiguration() {
        return queueConfiguration;
    }

    public Map<String, Object> getCustomConfiguration() {
        return customConfiguration;
    }

    public EngineConfiguration getEngineConfiguration() {
        return engineConfiguration;
    }

    public ThreadPoolConfiguration threadPoolConfiguration() {
        return threadPoolConfiguration;
    }
}

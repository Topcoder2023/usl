package com.gitee.usl.kernel.configure;

import java.util.function.Consumer;

/**
 * USL 配置类
 *
 * @author hongda.li
 */
public interface UslConfiguration {
    /**
     * 配置脚本引擎
     *
     * @param consumer 脚本引擎配置消费者
     * @return 脚本引擎配置
     */
    EngineConfiguration configEngine(Consumer<EngineConfiguration> consumer);

    /**
     * 配置线程池
     *
     * @param consumer 线程池配置消费者
     * @return 线程池配置
     */
    ThreadPoolConfiguration configThreadPool(Consumer<ThreadPoolConfiguration> consumer);
}

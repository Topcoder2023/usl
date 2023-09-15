package com.gitee.usl.infra.thread;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import com.gitee.usl.api.UslInitializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.ThreadPoolConfiguration;
import com.gitee.usl.kernel.configure.UslConfiguration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * USL 全局线程池执行器
 *
 * @author hongda.li
 */
@Order(Integer.MIN_VALUE)
public class UslExecutor implements UslInitializer {
    /**
     * USL 线程名称前缀
     */
    private static final String PREFIX = "USL_Executor";

    /**
     * 线程池
     */
    private ThreadPoolExecutor executor;

    /**
     * 提交线程任务
     *
     * @param runnable 线程任务
     */
    public void submit(Runnable runnable) {
        this.executor.submit(runnable);
    }

    @Override
    public void doInit(UslConfiguration uslConfiguration) {
        ThreadPoolConfiguration configuration = uslConfiguration.getThreadPoolConfiguration();

        this.executor = ExecutorBuilder.create()
                // 最大线程数
                .setMaxPoolSize(configuration.getMaxPoolSize())
                // 线程阻塞策略，由主线程来直接执行
                .setHandler(RejectPolicy.CALLER_RUNS.getValue())
                // 线程工厂
                .setThreadFactory(new NamedThreadFactory(PREFIX))
                // 核心线程数
                .setCorePoolSize(configuration.getCorePoolSize())
                // 线程等待队列大小
                .useArrayBlockingQueue(configuration.getQueueSize())
                // 是否允许超时线程
                .setAllowCoreThreadTimeOut(configuration.isAllowedTimeout())
                // 线程存活时间，单位：秒
                .setKeepAliveTime(configuration.getAliveTime(), configuration.getTimeUnit())
                // 构建线程池
                .build();

        configuration.setUslExecutor(this);
    }

    /**
     * 获取线程池
     *
     * @return 线程池实例
     */
    public ThreadPoolExecutor getExecutor() {
        return executor;
    }
}

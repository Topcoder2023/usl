package com.gitee.usl.infra.thread;

import cn.hutool.core.lang.Singleton;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import com.gitee.usl.api.service.UslEnvLoader;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * USL 全局线程池执行器
 *
 * @author hongda.li
 */
public class UslExecutor {
    /**
     * USL 线程名称前缀
     */
    private static final String PREFIX = "USL_Executor";

    private UslExecutor() {
    }

    private static ThreadPoolExecutor executor;

    /**
     * 提交线程任务
     *
     * @param runnable 线程任务
     */
    public static void submit(Runnable runnable) {
        executor().submit(runnable);
    }

    /**
     * 获取 USL 全局线程执行器
     * 若已经初始化则直接返回
     * 否则将进行初始化
     */
    public static ThreadPoolExecutor executor() {
        if (executor != null) {
            return executor;
        }

        // 双重检查锁
        synchronized (UslExecutor.class) {
            if (executor != null) {
                return executor;
            }

            UslEnvLoader envLoader = Singleton.get(UslEnvLoader.class);

            // 获取CPU核心数
            int processor = Runtime.getRuntime().availableProcessors();

            // 初始化线程池
            executor = ExecutorBuilder.create()
                    // 线程阻塞策略，由主线程来直接执行
                    .setHandler(RejectPolicy.CALLER_RUNS.getValue())
                    // 线程工厂
                    .setThreadFactory(new NamedThreadFactory(PREFIX))
                    // 核心线程数
                    .setCorePoolSize(envLoader.getAsInt("thread.size.core", processor))
                    // 最大线程数
                    .setMaxPoolSize(envLoader.getAsInt("thread.size.max", processor * 4))
                    // 是否允许超时线程
                    .setAllowCoreThreadTimeOut(envLoader.getAsBool("thread.allow-timeout", false))
                    // 线程等待队列大小
                    .useArrayBlockingQueue(envLoader.getAsInt("thread.size.queue", processor * 8))
                    // 线程存活时间，单位：秒
                    .setKeepAliveTime(envLoader.getAsInt("thread.alive-time", 60), TimeUnit.SECONDS)
                    // 构建线程池
                    .build();
        }

        return executor;
    }
}

package com.gitee.usl.infra.thread;

import cn.hutool.core.text.StrPool;
import com.gitee.usl.infra.constant.NumberConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 支持自定义线程名称的线程工厂
 *
 * @author hongda.li
 */
public class NamedThreadFactory implements ThreadFactory {
    /**
     * 线程创建成功时的信息
     */
    private static final String THREAD_CREATE = "Thread created success. {}";

    /**
     * 线程执行发生异常时的信息
     */
    private static final String THREAD_ERROR = "An error occurred during thread execution. [{} - {}]";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 线程名称的前缀
     */
    private final String prefix;

    /**
     * 线程名称的后缀，即创建线程的序列数
     */
    private final AtomicLong count;

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
        // 序列默认从0开始
        this.count = new AtomicLong(NumberConstant.ZERO);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(runnable);

        // 非守护线程
        thread.setDaemon(false);

        // 线程名称 = 线程自定义前缀 + 下划线 + 线程自增序列
        thread.setName(this.prefix + StrPool.UNDERLINE + count.getAndIncrement());

        // 线程执行发生异常时输出日志信息
        thread.setUncaughtExceptionHandler((t, e) -> logger.warn(THREAD_ERROR, t.getName(), e.getMessage()));

        // 线程创建完成
        logger.debug(THREAD_CREATE, thread.getName());

        return thread;
    }
}

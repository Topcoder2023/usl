package com.gitee.usl.infra.thread;

import cn.hutool.core.text.StrPool;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hongda.li
 */
@Slf4j
@Description("支持自定义线程名称的虚拟线程工厂")
public class NamedThreadFactory implements ThreadFactory {
    @Description("线程创建成功时的信息")
    private static final String THREAD_CREATE = "虚拟线程创建成功 - {}";

    @Description("线程执行发生异常时的信息")
    private static final String THREAD_ERROR = "虚拟线程执行出现错误 - [{} - {}]";

    @Description("线程名称的前缀")
    private final String prefix;

    @Description("线程名称的后缀，即创建线程的序列数")
    private final AtomicLong count = new AtomicLong(NumberConstant.ZERO);

    public NamedThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = Thread.ofVirtual().unstarted(runnable);

        thread.setName(this.prefix + StrPool.UNDERLINE + count.getAndIncrement());

        thread.setUncaughtExceptionHandler((t, e) -> log.warn(THREAD_ERROR, t.getName(), e.getMessage()));

        log.debug(THREAD_CREATE, thread.getName());

        return thread;
    }
}

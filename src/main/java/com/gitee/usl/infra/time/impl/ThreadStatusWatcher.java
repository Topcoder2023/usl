package com.gitee.usl.infra.time.impl;

import com.gitee.usl.infra.thread.UslExecutor;
import com.gitee.usl.infra.time.FixedRateTimedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池状态监听器
 *
 * @author hongda.li
 */
public class ThreadStatusWatcher implements FixedRateTimedTask {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public long cycle() {
        // 每30秒输出一次线程池状态
        return 30;
    }

    @Override
    public void doTask() {
        final ThreadPoolExecutor executor = UslExecutor.executor();

        logger.debug("[{}] => [TC:{} - CTC:{} - AC:{} - WC:{}]", executor.getClass().getSimpleName(),
                executor.getTaskCount(),
                executor.getCompletedTaskCount(),
                executor.getActiveCount(),
                executor.getQueue().size());
    }
}

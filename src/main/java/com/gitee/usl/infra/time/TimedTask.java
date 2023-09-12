package com.gitee.usl.infra.time;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface TimedTask {
    /**
     * 执行任务的逻辑实现
     */
    void doTask();

    /**
     * 初始延迟时间
     * 默认为 0，即立刻执行
     *
     * @return 延迟时间
     */
    default long initDelay() {
        return NumberConstant.ZERO;
    }

    /**
     * 时间单位
     * 默认为秒
     *
     * @return 时间单位
     */
    default TimeUnit unit() {
        return TimeUnit.SECONDS;
    }
}

package com.gitee.usl.infra.time;

/**
 * 固定延迟时间的定时任务
 *
 * @author hongda.li
 */
public interface FixedDelayTimedTask extends TimedTask {
    /**
     * 延迟时间
     *
     * @return 延迟时间
     */
    long delay();
}

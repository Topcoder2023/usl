package com.gitee.usl.infra.time;

/**
 * 固定延迟时间的定时任务
 *
 * @author hongda.li
 */
public interface FixedDelayTimedTask extends TimedTask {
    /**
     * 延迟时间
     * 例如：delay() 返回为 5，且 unit 为 SECONDS
     * 则每个任务执行完成后开始计时
     * 五秒后执行下一个任务
     * 因此每个任务的执行时间间隔是固定的，但是每个任务的执行频率不固定
     *
     * @return 延迟时间
     */
    long delay();
}

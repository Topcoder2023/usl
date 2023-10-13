package com.gitee.usl.infra.time;

/**
 * 固定执行频率的定时任务
 *
 * @author hongda.li
 */
public interface FixedRateTimedTask extends TimedTask {
    /**
     * 执行周期
     * 例如：cycle() 返回 10，且 unit 为 SECONDS
     * 则每个任务执行开始后开始计时
     * 10秒后执行下一个任务
     * 即使当前任务没有执行完成也会进入下一个周期
     * 因此每个任务的执行频率是固定的，但是任务之前的时间间隔不固定
     *
     * @return 执行周期数
     */
    long cycle();
}

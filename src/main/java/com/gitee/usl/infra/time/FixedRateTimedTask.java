package com.gitee.usl.infra.time;

/**
 * 固定执行频率的定时任务
 *
 * @author hongda.li
 */
public interface FixedRateTimedTask extends TimedTask {
    /**
     * 执行周期
     *
     * @return 执行周期数
     */
    long cycle();
}

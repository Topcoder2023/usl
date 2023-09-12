package com.gitee.usl.infra.time;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.infra.thread.NamedThreadFactory;
import com.gitee.usl.infra.utils.SpiServiceUtil;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 定时任务管理器
 *
 * @author hongda.li
 */
public class TimedTaskManager implements Initializer {
    public static final String PREFIX = "USL_Timed_Executor";
    private final ScheduledExecutorService executor;
    private final List<TimedTask> taskList;

    public TimedTaskManager() {
        taskList = SpiServiceUtil.loadSortedService(TimedTask.class);
        executor = new ScheduledThreadPoolExecutor(taskList.size(), new NamedThreadFactory(PREFIX));
    }

    @Override
    public void doInit() {
        // 初始化固定执行频率的定时任务
        taskList.stream()
                .filter(FixedRateTimedTask.class::isInstance)
                .map(FixedRateTimedTask.class::cast)
                .forEach(fixed -> executor.scheduleAtFixedRate(fixed::doTask,
                        fixed.initDelay(),
                        fixed.cycle(),
                        fixed.unit()));

        // 初始化固定延迟时间的定时任务
        taskList.stream()
                .filter(FixedDelayTimedTask.class::isInstance)
                .map(FixedDelayTimedTask.class::cast)
                .forEach(fixed -> executor.scheduleWithFixedDelay(fixed::doTask,
                        fixed.initDelay(),
                        fixed.delay(),
                        fixed.unit()));

        // 初始化仅执行一次的定时任务
        taskList.stream()
                .filter(FixedOnceTimedTask.class::isInstance)
                .map(FixedOnceTimedTask.class::cast)
                .forEach(fixed -> executor.schedule(fixed::doTask,
                        fixed.initDelay(),
                        fixed.unit()));
    }
}

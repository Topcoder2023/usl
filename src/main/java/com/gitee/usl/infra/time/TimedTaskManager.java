package com.gitee.usl.infra.time;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.thread.NamedThreadFactory;
import com.gitee.usl.infra.utils.SpiServiceUtil;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

/**
 * 定时任务管理器
 *
 * @author hongda.li
 */
@Order
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
        this.takeWhile(FixedRateTimedTask.class, fixed -> executor.scheduleAtFixedRate(fixed::doTask,
                fixed.initDelay(),
                fixed.cycle(),
                fixed.unit()));

        // 初始化固定延迟时间的定时任务
        this.takeWhile(FixedDelayTimedTask.class, fixed -> executor.scheduleWithFixedDelay(fixed::doTask,
                fixed.initDelay(),
                fixed.delay(),
                fixed.unit()));

        // 初始化仅执行一次的定时任务
        this.takeWhile(FixedOnceTimedTask.class, fixed -> executor.schedule(fixed::doTask,
                fixed.initDelay(),
                fixed.unit()));
    }

    /**
     * 过滤出指定类型的任务调度实例
     * 并根据传入的消费者进行消费
     *
     * @param type     指定的任务调度类型
     * @param consumer 指定的消费者
     * @param <T>      任务调度实例泛型
     */
    private <T extends TimedTask> void takeWhile(Class<T> type, Consumer<T> consumer) {
        taskList.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .forEach(consumer);
    }
}

package com.gitee.usl.infra.time;

import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.thread.NamedThreadFactory;
import com.gitee.usl.infra.utils.ServiceSearcher;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

/**
 * @author hongda.li
 */
@Getter
@Description("定时任务管理器")
@Order(Integer.MAX_VALUE - 100)
@AutoService(Initializer.class)
public class TimedTaskInitializer implements Initializer {

    @Description("线程工厂")
    private static final ThreadFactory NAME_FACTORY = new NamedThreadFactory("定时任务线程");

    @Description("任务集合")
    private List<TimedTask> taskList;

    @Description("定时任务线程池")
    private ScheduledExecutorService executor;

    @Override
    public void doInit(Configuration configuration) {
        taskList = ServiceSearcher.searchAll(TimedTask.class);
        executor = new ScheduledThreadPoolExecutor(taskList.size(), NAME_FACTORY);

        this.takeWhile(FixedRateTimedTask.class, fixed -> executor.scheduleAtFixedRate(() -> fixed.doTask(configuration),
                fixed.initDelay(),
                fixed.cycle(),
                fixed.unit()));

        this.takeWhile(FixedDelayTimedTask.class, fixed -> executor.scheduleWithFixedDelay(() -> fixed.doTask(configuration),
                fixed.initDelay(),
                fixed.delay(),
                fixed.unit()));

        this.takeWhile(FixedOnceTimedTask.class, fixed -> executor.schedule(() -> fixed.doTask(configuration),
                fixed.initDelay(),
                fixed.unit()));
    }

    @Description("过滤出指定类型的任务调度实例")
    private <T extends TimedTask> void takeWhile(Class<T> type, Consumer<T> consumer) {
        taskList.stream()
                .filter(type::isInstance)
                .map(type::cast)
                .forEach(consumer);
    }

}

package com.gitee.usl.infra.thread;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.time.FixedRateTimedTask;
import com.gitee.usl.infra.time.TimedTask;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hongda.li
 */
@Slf4j
@Description("线程池状态监控")
@AutoService(TimedTask.class)
public class ThreadStatusWatcher implements FixedRateTimedTask {

    @Override
    public long cycle() {
        return 60;
    }

    @Override
    public void doTask(Configuration configuration) {

        @Description("USL线程池")
        ThreadPoolExecutor executor = configuration.getExecutorConfig()
                .getPoolInitializer()
                .getExecutor();

        log.debug("[USL线程池 & {}] - [任务数:{} - 总任务数:{} - 活跃线程数:{}]", configuration.getRunner().getName(),
                executor.getTaskCount(),
                executor.getCompletedTaskCount(),
                executor.getActiveCount());
    }
}

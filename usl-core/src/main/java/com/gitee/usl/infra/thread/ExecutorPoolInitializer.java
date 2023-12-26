package com.gitee.usl.infra.thread;

import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.RejectPolicy;
import com.gitee.usl.api.Initializer;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.configure.ExecutorConfig;
import com.gitee.usl.kernel.configure.Configuration;
import com.google.auto.service.AutoService;
import lombok.Getter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hongda.li
 */
@Getter
@Order(Integer.MIN_VALUE)
@Description("线程池初始化器")
@AutoService(Initializer.class)
public class ExecutorPoolInitializer implements Initializer {

    @Description("线程工厂")
    private static final ThreadFactory NAMED_THREAD_FACTORY = new NamedThreadFactory("USL线程");

    @Description("线程池")
    private ThreadPoolExecutor executor;

    @Override
    public void doInit(Configuration configuration) {
        ExecutorConfig config = configuration.getExecutorConfig();

        this.executor = ExecutorBuilder.create()
                .setThreadFactory(NAMED_THREAD_FACTORY)
                .setMaxPoolSize(config.getMaxPoolSize())
                .setHandler(RejectPolicy.CALLER_RUNS.getValue())
                .setCorePoolSize(config.getCorePoolSize())
                .useArrayBlockingQueue(config.getQueueSize())
                .setAllowCoreThreadTimeOut(config.isAllowedTimeout())
                .setKeepAliveTime(config.getAliveTime(), config.getTimeUnit())
                .build();

        config.setPoolInitializer(this);
    }

}

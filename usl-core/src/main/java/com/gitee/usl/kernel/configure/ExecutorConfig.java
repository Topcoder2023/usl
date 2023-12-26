package com.gitee.usl.kernel.configure;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.thread.ExecutorPoolInitializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@Description("线程池配置类")
public class ExecutorConfig {

    @Description("USL配置类")
    private final Configuration configuration;

    @Description("核心线程数量")
    private int corePoolSize = 8;

    @Description("最大线程数量")
    private int maxPoolSize = 16;

    @Description("允许线程超时")
    private boolean allowedTimeout = false;

    @Description("线程队列大小")
    private int queueSize = 1024;

    @Description("线程存货时间")
    private int aliveTime = 60;

    @Description("线程时间单位")
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    @Description("线程池初始化器")
    private ExecutorPoolInitializer poolInitializer;

    public ExecutorConfig(Configuration configuration) {
        this.configuration = configuration;
    }

}

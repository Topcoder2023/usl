package com.gitee.usl.kernel.configure;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.structure.StringMap;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Getter
@Setter
@Accessors(chain = true)
@Description("USL 配置类")
public final class Configuration {

    @Description("当前配置对应的USL实例")
    private USLRunner runner;

    @Description("自定义扩展配置类")
    private final StringMap customConfig = new StringMap();

    @Description("缓存配置类")
    private final CacheConfig cacheConfig = new CacheConfig(this);

    @Description("编译队列配置类")
    private final QueueConfig queueConfig = new QueueConfig(this);

    @Description("脚本引擎配置类")
    private final EngineConfig engineConfig = new EngineConfig(this);

    @Description("线程池配置类")
    private final ExecutorConfig executorConfig = new ExecutorConfig(this);

}

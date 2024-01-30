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
    private final StringMap<Object> customConfig = new StringMap<>();

    @Description("脚本引擎配置类")
    private final EngineConfig engineConfig = new EngineConfig(this);

    @Description("缓存配置类")
    private final CompilerConfig compilerConfig = new CompilerConfig(this);

}

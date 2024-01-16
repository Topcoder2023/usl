package com.gitee.usl.kernel.configure;

import com.gitee.usl.api.ScriptCompiler;
import com.gitee.usl.api.annotation.Description;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Duration;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class CompilerConfig {

    @Description("USL配置类")
    private final Configuration configuration;

    @Description("脚本编译器")
    private ScriptCompiler scriptCompiler;

    @Description("脚本缓存容量")
    private int size = 2 << 10;

    @Description("访问后的失效时间")
    private Duration duration = Duration.ofHours(24L);

    public CompilerConfig(Configuration configuration) {
        this.configuration = configuration;
    }

}

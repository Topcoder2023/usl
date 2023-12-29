package com.gitee.usl.kernel.queue;

import cn.hutool.core.util.IdUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.structure.StringMap;
import com.gitee.usl.kernel.configure.Configuration;
import com.googlecode.aviator.Expression;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
@Description("编译事件")
public class CompileEvent {

    @Description("脚本事件ID")
    private final String eventId = IdUtil.fastSimpleUUID();

    @Description("脚本内容")
    private String content;

    @Description("初始变量")
    private StringMap initEnv;

    @Description("脚本编译值")
    private Expression expression;

    @Description("USL配置类")
    private Configuration configuration;

}

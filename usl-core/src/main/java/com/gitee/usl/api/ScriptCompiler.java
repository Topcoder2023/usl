package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.domain.Param;

/**
 * @author hongda.li
 */
@FunctionalInterface
@Description("脚本存储器")
public interface ScriptCompiler {

    @Description("存储参数")
    void compile(@Description("脚本参数信息") Param param);

}

package com.gitee.usl.grammar.script;

import com.gitee.usl.api.annotation.Description;

import java.util.Map;

/**
 * @author hongda.li
 */
@Description("脚本接口")
public interface Script {
    @Description("直接执行脚本")
    Object execute();

    @Description("根据上下文执行脚本")
    Object execute(Map<String, Object> context);

}

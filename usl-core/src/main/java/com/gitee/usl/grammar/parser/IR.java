package com.gitee.usl.grammar.parser;

import com.gitee.usl.api.annotation.Description;

/**
 * @author hongda.li
 */
@FunctionalInterface
@Description("指令接口")
public interface IR {

    @Description("执行指令")
    void eval(@Description("指令上下文") InterpretContext context);

}

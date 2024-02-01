package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.runtime.type._Object;

import java.util.Map;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface FunctionMissing {

    @Description("函数兜底逻辑")
    _Object onFunctionMissing(@Description("函数名称") final String name,
                              @Description("上下文环境") final Map<String, Object> env,
                              @Description("函数参数") final _Object... arguments);

}

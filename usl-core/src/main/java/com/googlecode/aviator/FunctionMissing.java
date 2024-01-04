package com.googlecode.aviator;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.exception.USLNotFoundException;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface FunctionMissing {

    @Description("函数兜底逻辑")
    AviatorObject onFunctionMissing(@Description("函数名称") final String name,
                                    @Description("上下文环境") final Map<String, Object> env,
                                    @Description("函数参数") final AviatorObject... arguments);

    FunctionMissing DEFAULT = (name, env, args) -> {
        throw new USLNotFoundException("无法加载此函数 - {}", name);
    };

}

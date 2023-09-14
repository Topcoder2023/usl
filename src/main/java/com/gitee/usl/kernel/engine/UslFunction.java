package com.gitee.usl.kernel.engine;

import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.infra.proxy.UslInvocation;
import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * USL 函数实例
 *
 * @author hongda.li
 */
public class UslFunction extends AbstractVariadicFunction {
    private final UslFunctionDefinition definition;

    public UslFunction(UslFunctionDefinition definition) {
        this.definition = definition;
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        UslInvocation<?> invocation = definition.getInvocation();
        return FunctionUtils.wrapReturn(ReflectUtil.invoke(invocation.target(), invocation.method()));
    }

    @Override
    public String getName() {
        return this.definition.getName();
    }

    public UslFunctionDefinition getDefinition() {
        return definition;
    }
}

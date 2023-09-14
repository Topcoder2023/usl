package com.gitee.usl.kernel.engine;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
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
        return null;
    }

    @Override
    public String getName() {
        return this.definition.getName();
    }

    public UslFunctionDefinition getDefinition() {
        return definition;
    }
}

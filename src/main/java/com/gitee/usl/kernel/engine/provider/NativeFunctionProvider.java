package com.gitee.usl.kernel.engine.provider;

import com.gitee.usl.kernel.engine.UslAviatorProxyFunction;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
public class NativeFunctionProvider extends AbstractFunctionProvider {
    @Override
    protected List<UslFunctionDefinition> class2Definition(Class<?> clz) {
        return Collections.emptyList();
    }

    @Override
    protected AviatorFunction definition2Func(UslFunctionDefinition definition) {
        // 创建 USL-Aviator 代理函数
        // 动态代理 AviatorFunction 接口中的 call() 方法
        return new UslAviatorProxyFunction(definition, definition.getInvocation().target()).createProxy();
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return AviatorFunction.class.isAssignableFrom(clz);
    }
}

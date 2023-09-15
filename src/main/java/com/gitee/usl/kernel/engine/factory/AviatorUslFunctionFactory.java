package com.gitee.usl.kernel.engine.factory;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.infra.proxy.UslInvocation;
import com.gitee.usl.infra.proxy.UslMethodInterceptor;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.googlecode.aviator.runtime.type.AviatorFunction;

/**
 * 代理函数工厂
 * 负责创建实现了 AviatorFunction 接口的函数代理实例
 * 通过代理 AviatorFunction 接口的实现类而创建
 *
 * @author hongda.li
 */
public class AviatorUslFunctionFactory extends AbstractUslFunctionFactory {

    @Override
    public AviatorFunction create(UslFunctionDefinition definition) {
        UslInvocation<?> invocation = definition.getInvocation();

        // 仅支持实现了 AviatorFunction 的原生实现
        if (!isMatch(invocation.targetType())) {
            return null;
        }

        return new UslMethodInterceptor<>(invocation.target(), AviatorFunction.class) {
            @Override
            protected Object intercept(UslInvocation<AviatorFunction> uslInvocation, Object proxy) {
                return null;
            }
        }.createProxy();
    }

    private boolean isMatch(Class<?> clz) {
        return ClassUtil.isNormalClass(clz) && AviatorFunction.class.isAssignableFrom(clz);
    }
}

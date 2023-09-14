package com.gitee.usl.kernel.engine.factory;

import cn.hutool.core.util.ClassUtil;
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
    public boolean supported(UslFunctionDefinition definition) {
        return AviatorFunction.class.isAssignableFrom(definition.getInvocation().targetType())
                && ClassUtil.isNormalClass(definition.getInvocation().targetType());
    }

    @Override
    public AviatorFunction create(UslFunctionDefinition definition) {
        return null;
    }
}

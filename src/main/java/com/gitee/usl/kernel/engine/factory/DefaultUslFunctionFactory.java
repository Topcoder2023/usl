package com.gitee.usl.kernel.engine.factory;

import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.googlecode.aviator.runtime.type.AviatorFunction;

/**
 * USL 默认函数工厂
 * 负责创建 UslFunction 类型函数实例
 *
 * @author hongda.li
 */
public class DefaultUslFunctionFactory extends AbstractUslFunctionFactory {
    @Override
    public boolean supported(UslFunctionDefinition definition) {
        return !AviatorFunction.class.isAssignableFrom(definition.getInvocation().targetType())
                && ClassUtil.isNormalClass(definition.getInvocation().targetType());
    }

    @Override
    public AviatorFunction create(UslFunctionDefinition definition) {
        return null;
    }
}

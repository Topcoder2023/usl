package com.gitee.usl.kernel.engine.factory;

import com.gitee.usl.kernel.engine.UslFunctionDefinition;
import com.googlecode.aviator.runtime.type.AviatorFunction;

/**
 * Web USL 函数工厂
 * 负责创建声明式接口类型的函数实例
 * 通过代理声明书接口而创建
 *
 * @author hongda.li
 */
public class WebUslFunctionFactory extends AbstractUslFunctionFactory {
    @Override
    public boolean supported(UslFunctionDefinition definition) {
        return false;
    }

    @Override
    public AviatorFunction create(UslFunctionDefinition definition) {
        return null;
    }
}

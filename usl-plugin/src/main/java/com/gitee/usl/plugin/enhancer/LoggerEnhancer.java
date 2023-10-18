package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.impl.LoggerPlugin;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE - 10)
@AutoService(FunctionEnhancer.class)
public class LoggerEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        nf.plugins().install(Singleton.get(LoggerPlugin.class));
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(Singleton.get(LoggerPlugin.class));
    }
}

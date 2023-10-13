package com.gitee.usl.plugin.enhancer;

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
    private final LoggerPlugin singletonPlugin = new LoggerPlugin();

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        nf.plugins().install(singletonPlugin);
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(singletonPlugin);
    }
}

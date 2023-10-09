package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.app.plugin.LoggerPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.api.FunctionEnhancer;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE - 10)
@AutoService(FunctionEnhancer.class)
public class LoggerEnhancer extends AbstractFunctionEnhancer {
    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        nf.plugins().install(new LoggerPlugin());
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(new LoggerPlugin());
    }
}

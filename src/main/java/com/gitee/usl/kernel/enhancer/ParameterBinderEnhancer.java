package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.app.plugin.ParameterBinderPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.api.FunctionEnhancer;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Order(Integer.MIN_VALUE + 10)
@AutoService(FunctionEnhancer.class)
public class ParameterBinderEnhancer extends AbstractFunctionEnhancer {
    private final ParameterBinderPlugin singletonPlugin = new ParameterBinderPlugin();

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(singletonPlugin);
    }
}

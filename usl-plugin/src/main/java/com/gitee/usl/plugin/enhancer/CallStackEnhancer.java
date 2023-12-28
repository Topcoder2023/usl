package com.gitee.usl.plugin.enhancer;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.impl.stack.CallStackPlugin;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(FunctionEnhancer.class)
public class CallStackEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {

        USLRunner runner = nf.definition().getRunner();

        nf.plugins().install(new CallStackPlugin(runner));
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {

        USLRunner runner = af.definition().getRunner();

        af.plugins().install(new CallStackPlugin(runner));
    }
}

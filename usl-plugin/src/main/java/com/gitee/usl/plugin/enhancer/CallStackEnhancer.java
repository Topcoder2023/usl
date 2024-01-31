package com.gitee.usl.plugin.enhancer;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.impl.stack.CallStackPlugin;

/**
 * @author hongda.li
 */
public class CallStackEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {

        USLRunner runner = nf.definition().getRunner();

        if (Boolean.TRUE.equals(runner.getConfiguration().getDebug())) {
            nf.plugins().install(new CallStackPlugin(runner));
        }
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {

        USLRunner runner = af.definition().getRunner();

        if (Boolean.TRUE.equals(runner.getConfiguration().getDebug())) {
            af.plugins().install(new CallStackPlugin(runner));
        }
    }
}

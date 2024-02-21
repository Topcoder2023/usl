package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.plugin.ParameterBinderPlugin;

/**
 * @author hongda.li
 */
public class ParameterBinderEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(Singleton.get(ParameterBinderPlugin.class));
    }

}

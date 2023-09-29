package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.app.plugin.ParameterBinderPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.UslFunctionEnhancer;
import com.googlecode.aviator.runtime.type.AviatorFunction;

/**
 * @author hongda.li
 */
@Order(Integer.MIN_VALUE + 10)
public class ParameterBinderEnhancer implements UslFunctionEnhancer {
    @Override
    public void enhance(AviatorFunction function) {
        if (!AnnotatedFunction.class.isAssignableFrom(function.getClass())) {
            return;
        }

        ((AnnotatedFunction) function).plugins().add(new ParameterBinderPlugin());
    }
}

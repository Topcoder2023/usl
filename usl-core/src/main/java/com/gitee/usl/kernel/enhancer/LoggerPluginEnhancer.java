package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.annotation.EnableLogger;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.plugin.LoggerPlugin;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE - 10)
@AutoService(FunctionEnhancer.class)
public class LoggerPluginEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        if (AnnotationUtil.hasAnnotation(af.definition().getMethodMeta().getMethod(), EnableLogger.class)) {
            af.plugins().install(Singleton.get(LoggerPlugin.class));
        }
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        if (AnnotationUtil.hasAnnotation(nf.definition().getMethodMeta().getTargetType(), EnableLogger.class)) {
            nf.plugins().install(Singleton.get(LoggerPlugin.class));
        }
    }

}

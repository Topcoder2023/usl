package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.annotation.EnableLogger;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.plugin.LoggerPlugin;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE - 10)
public class LoggerPluginEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        if (AnnotationUtil.hasAnnotation(af.definition().getMethodMeta().method(), EnableLogger.class)) {
            af.plugins().install(Singleton.get(LoggerPlugin.class));
        }
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        if (AnnotationUtil.hasAnnotation(nf.definition().getMethodMeta().targetType(), EnableLogger.class)) {
            nf.plugins().install(Singleton.get(LoggerPlugin.class));
        }
    }

}

package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Limited;
import com.gitee.usl.plugin.impl.RateLimitPlugin;

/**
 * @author hongda.li
 */
public class RateLimitEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Limited limited = AnnotationUtil.getAnnotation(af.definition().getMethodMeta().method(), Limited.class);
        if (limited == null) {
            return;
        }

        af.plugins().install(new RateLimitPlugin(limited.value(), limited.timeout(), limited.unit()));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Limited limited = AnnotationUtil.getAnnotation(nf.definition().getMethodMeta().targetType(), Limited.class);
        if (limited == null) {
            return;
        }

        nf.plugins().install(new RateLimitPlugin(limited.value(), limited.timeout(), limited.unit()));
    }
}

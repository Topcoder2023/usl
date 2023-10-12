package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Limited;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.app.plugin.RateLimitPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Order(Integer.MIN_VALUE + 10)
@AutoService(FunctionEnhancer.class)
public class RateLimitEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Limited limited = AnnotationUtil.getAnnotation(af.definition().methodMeta().method(), Limited.class);
        if (limited == null) {
            return;
        }

        af.plugins().install(new RateLimitPlugin(limited.value(), limited.timeout(), limited.unit()));
    }
}

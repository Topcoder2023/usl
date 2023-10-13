package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Cacheable;
import com.gitee.usl.plugin.impl.CacheablePlugin;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@AutoService(FunctionEnhancer.class)
public class CacheableEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Cacheable cacheable = AnnotationUtil.getAnnotation(af.definition().methodMeta().method(), Cacheable.class);
        if (cacheable == null) {
            return;
        }

        af.plugins().install(new CacheablePlugin(cacheable.expired(), cacheable.unit()));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Cacheable cacheable = AnnotationUtil.getAnnotation(nf.definition().methodMeta().targetType(), Cacheable.class);
        if (cacheable == null) {
            return;
        }

        nf.plugins().install(new CacheablePlugin(cacheable.expired(), cacheable.unit()));
    }
}

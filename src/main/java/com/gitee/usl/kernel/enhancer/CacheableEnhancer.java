package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Cacheable;
import com.gitee.usl.app.plugin.CacheablePlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
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
}

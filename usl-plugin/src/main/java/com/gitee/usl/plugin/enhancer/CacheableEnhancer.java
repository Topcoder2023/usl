package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Cacheable;
import com.gitee.usl.plugin.api.CacheKeyGenerator;
import com.gitee.usl.plugin.impl.CacheablePlugin;
import com.google.auto.service.AutoService;

import java.util.Optional;

/**
 * @author hongda.li
 */
@AutoService(FunctionEnhancer.class)
public class CacheableEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Cacheable cacheable = AnnotationUtil.getAnnotation(af.definition().methodMeta().getMethod(), Cacheable.class);
        if (cacheable == null) {
            return;
        }

        af.plugins().install(this.newCacheablePlugin(cacheable));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Cacheable cacheable = AnnotationUtil.getAnnotation(nf.definition().methodMeta().getTargetType(), Cacheable.class);
        if (cacheable == null) {
            return;
        }

        nf.plugins().install(this.newCacheablePlugin(cacheable));
    }

    private CacheablePlugin newCacheablePlugin(Cacheable cacheable) {
        CacheKeyGenerator generator = Optional.ofNullable(cacheable.generator())
                .filter(ClassUtil::isNormalClass)
                .map(Singleton::get)
                .map(CacheKeyGenerator.class::cast)
                .orElse(new CacheablePlugin.DefaultCacheKeyGenerator());

        return new CacheablePlugin(cacheable.maxSize(), cacheable.expired(), cacheable.unit(), generator);
    }
}

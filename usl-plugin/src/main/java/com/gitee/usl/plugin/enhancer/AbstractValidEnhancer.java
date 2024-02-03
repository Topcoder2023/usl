package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.TypeUtil;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * 抽象注解函数增强器
 *
 * @author hongda.li
 */
@SuppressWarnings("unchecked")
public abstract class AbstractValidEnhancer<A extends Annotation> extends AbstractFunctionEnhancer {

    /**
     * 提供插件
     *
     * @return 插件实例
     */
    protected abstract Plugin provide();

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        if (Arrays.stream(nf.definition()
                        .getMethodMeta()
                        .method()
                        .getParameters())
                .anyMatch(param -> AnnotationUtil.hasAnnotation(param, (Class<A>) TypeUtil.getTypeArgument(this.getClass())))) {
            nf.plugins().install(this.provide());
        }
    }

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        if (Arrays.stream(af.definition()
                        .getMethodMeta()
                        .method()
                        .getParameters())
                .anyMatch(param -> AnnotationUtil.hasAnnotation(param, (Class<A>) TypeUtil.getTypeArgument(this.getClass())))) {
            af.plugins().install(this.provide());
        }
    }
}

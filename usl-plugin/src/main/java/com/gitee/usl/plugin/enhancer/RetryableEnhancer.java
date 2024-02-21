package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Retryable;
import com.gitee.usl.plugin.api.RetryBuilderFactory;
import com.gitee.usl.plugin.impl.RetryPlugin;
import com.github.rholder.retry.*;

import java.util.Optional;

/**
 * @author hongda.li
 */
public class RetryableEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Retryable retryable = AnnotationUtil.getAnnotation(af.definition().getMethodMeta().method(), Retryable.class);
        if (retryable == null) {
            return;
        }

        // 安装重试插件
        af.plugins().install(this.newRetryPlugin(retryable));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Retryable retryable = AnnotationUtil.getAnnotation(nf.definition().getMethodMeta().targetType(), Retryable.class);
        if (retryable == null) {
            return;
        }

        // 安装重试插件
        nf.plugins().install(this.newRetryPlugin(retryable));
    }

    private RetryPlugin newRetryPlugin(Retryable retryable) {
        // 支持为每一个函数单独设置重试器
        // 但通一个class对应的实例为单例
        Retryer<Object> retryer = Optional.ofNullable(retryable.value())
                .filter(ClassUtil::isNormalClass)
                .map(Singleton::get)
                .map(RetryBuilderFactory.class::cast)
                .map(RetryBuilderFactory::create)
                .orElse(new RetryPlugin.DefaultRetryBuilderFactory(retryable).create());

        return new RetryPlugin(retryer);
    }
}

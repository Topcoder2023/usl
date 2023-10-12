package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.RetryBuilderFactory;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.api.annotation.Retryable;
import com.gitee.usl.app.plugin.RetryPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.github.rholder.retry.*;
import com.google.auto.service.AutoService;

import java.util.Optional;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE)
@AutoService(FunctionEnhancer.class)
public class RetryableEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Retryable retryable = AnnotationUtil.getAnnotation(af.definition().methodMeta().method(), Retryable.class);
        if (retryable == null) {
            return;
        }

        // 支持为每一个函数单独设置重试器
        // 但通一个class对应的实例为单例
        Retryer<Object> retryer = Optional.ofNullable(retryable.value())
                .filter(ClassUtil::isNormalClass)
                .map(Singleton::get)
                .map(RetryBuilderFactory.class::cast)
                .map(RetryBuilderFactory::create)
                .orElse(new RetryPlugin.DefaultRetryBuilderFactory(retryable).create());

        // 安装重试插件
        af.plugins().install(new RetryPlugin(retryer));
    }
}
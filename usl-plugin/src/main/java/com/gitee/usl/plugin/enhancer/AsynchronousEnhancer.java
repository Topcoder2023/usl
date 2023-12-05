package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.infra.thread.ExecutorPoolManager;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.configure.ExecutorConfiguration;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Asynchronous;
import com.gitee.usl.plugin.impl.AsyncPlugin;
import com.google.auto.service.AutoService;

import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE)
@AutoService(FunctionEnhancer.class)
public class AsynchronousEnhancer extends AbstractFunctionEnhancer {
    private static final Object PLACE_HOLDER = new Object();

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Asynchronous async = AnnotationUtil.getAnnotation(af.definition().methodMeta().method(), Asynchronous.class);
        if (async == null) {
            return;
        }

        // 安装异步插件
        af.plugins().install(this.newAsyncPlugin(async));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Asynchronous async = AnnotationUtil.getAnnotation(nf.definition().methodMeta().targetType(), Asynchronous.class);
        if (async == null) {
            return;
        }

        // 安装异步插件
        nf.plugins().install(this.newAsyncPlugin(async));
    }

    private AsyncPlugin newAsyncPlugin(Asynchronous async) {
        Object exists = Singleton.get(async.beanName(), () -> PLACE_HOLDER);
        ThreadPoolExecutor executor;

        if (PLACE_HOLDER.equals(exists)) {
            executor = Optional.ofNullable(USLRunner.findRunnerByName(async.value()))
                    .map(USLRunner::configuration)
                    .map(Configuration::configExecutor)
                    .map(ExecutorConfiguration::executorManager)
                    .map(ExecutorPoolManager::executor)
                    .orElse(null);
        } else {
            executor = exists instanceof ThreadPoolExecutor ? (ThreadPoolExecutor) exists : null;
        }

        Assert.notNull(executor, "@Async thread pool executor can not be found.");

        return new AsyncPlugin(executor);
    }
}

package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Asynchronous;
import com.gitee.usl.app.plugin.AsyncPlugin;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.google.auto.service.AutoService;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author hongda.li
 */
@AutoService(FunctionEnhancer.class)
public class AsynchronousEnhancer extends AbstractFunctionEnhancer {

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
        UslConfiguration configuration = UslRunner.findRunnerByName(async.value()).configuration();
        ThreadPoolExecutor executor = configuration.configThreadPool().executorManager().executor();
        return new AsyncPlugin(executor);
    }
}

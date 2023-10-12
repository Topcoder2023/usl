package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import com.gitee.usl.UslRunner;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Asynchronous;
import com.gitee.usl.app.plugin.AsyncPlugin;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
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

        // 根据异步注解指定的执行器名称获取其线程池实例
        UslConfiguration configuration = UslRunner.findRunnerByName(async.value()).configuration();
        ThreadPoolExecutor executor = configuration.configThreadPool().executorManager().executor();

        // 安装异步插件
        af.plugins().install(new AsyncPlugin(executor));
    }
}

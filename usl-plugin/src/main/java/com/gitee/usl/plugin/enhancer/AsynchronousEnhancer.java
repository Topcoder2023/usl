package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Async;
import com.gitee.usl.plugin.impl.AsyncPlugin;

/**
 * @author hongda.li
 */
@Order(Integer.MAX_VALUE)
public class AsynchronousEnhancer extends AbstractFunctionEnhancer {
    private static final Object PLACE_HOLDER = new Object();

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Async async = AnnotationUtil.getAnnotation(af.definition().getMethodMeta().getMethod(), Async.class);
        if (async == null) {
            return;
        }

        // 安装异步插件
        af.plugins().install(Singleton.get(AsyncPlugin.class));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Async async = AnnotationUtil.getAnnotation(nf.definition().getMethodMeta().getTargetType(), Async.class);
        if (async == null) {
            return;
        }

        // 安装异步插件
        nf.plugins().install(Singleton.get(AsyncPlugin.class));
    }
}

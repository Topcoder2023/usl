package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.api.TimeWatchListener;
import com.gitee.usl.api.annotation.TimeWatchable;
import com.gitee.usl.app.plugin.TimeWatchPlugin;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.google.auto.service.AutoService;

import java.util.Optional;

/**
 * @author hongda.li
 */
@AutoService(FunctionEnhancer.class)
public class TimeWatchEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        TimeWatchable watchable = AnnotationUtil.getAnnotation(af.definition().methodMeta().method(), TimeWatchable.class);
        if (watchable == null) {
            return;
        }

        final TimeWatchListener listener = Optional.ofNullable(watchable.listener())
                .filter(ClassUtil::isNormalClass)
                .map(Singleton::get)
                .map(TimeWatchListener.class::cast)
                .orElse(new TimeWatchPlugin.DefaultTimeWatchListener(watchable.unit()));

        af.plugins().install(new TimeWatchPlugin(watchable.threshold(), watchable.unit(), listener));
    }
}

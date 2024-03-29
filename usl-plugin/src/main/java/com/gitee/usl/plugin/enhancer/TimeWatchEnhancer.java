package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.TimeWatchable;
import com.gitee.usl.plugin.api.TimeWatchListener;
import com.gitee.usl.plugin.impl.TimeWatchPlugin;

import java.util.Optional;

/**
 * @author hongda.li
 */
public class TimeWatchEnhancer extends AbstractFunctionEnhancer {

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        TimeWatchable watchable = AnnotationUtil.getAnnotation(af.definition().getMethodMeta().method(), TimeWatchable.class);
        if (watchable == null) {
            return;
        }

        af.plugins().install(this.newTimeWatchPlugin(watchable));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        TimeWatchable watchable = AnnotationUtil.getAnnotation(nf.definition().getMethodMeta().targetType(), TimeWatchable.class);
        if (watchable == null) {
            return;
        }

        nf.plugins().install(this.newTimeWatchPlugin(watchable));
    }

    private TimeWatchPlugin newTimeWatchPlugin(TimeWatchable watchable) {
        final TimeWatchListener listener = Optional.ofNullable(watchable.listener())
                .filter(ClassUtil::isNormalClass)
                .map(Singleton::get)
                .map(TimeWatchListener.class::cast)
                .orElse(new TimeWatchPlugin.DefaultTimeWatchListener(watchable.unit()));

        return new TimeWatchPlugin(watchable.threshold(), watchable.unit(), listener);
    }
}

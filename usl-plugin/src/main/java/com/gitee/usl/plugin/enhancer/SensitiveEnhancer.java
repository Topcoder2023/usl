package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.enhancer.AbstractFunctionEnhancer;
import com.gitee.usl.plugin.annotation.Sensitized;
import com.gitee.usl.plugin.impl.sensitive.SensitivePlugin;

/**
 * @author hongda.li
 */
public class SensitiveEnhancer extends AbstractFunctionEnhancer {
    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        Sensitized sensitized = AnnotationUtil.getAnnotation(af.definition().getMethodMeta().method(), Sensitized.class);
        if (sensitized == null) {
            return;
        }

        // 安装脱敏插件
        af.plugins().install(Singleton.get(SensitivePlugin.class, sensitized.value()));
    }

    @Override
    protected void enhanceNativeFunction(NativeFunction nf) {
        Sensitized sensitized = AnnotationUtil.getAnnotation(nf.definition().getMethodMeta().targetType(), Sensitized.class);
        if (sensitized == null) {
            return;
        }

        // 安装脱敏插件
        nf.plugins().install(Singleton.get(SensitivePlugin.class, sensitized.value()));
    }
}

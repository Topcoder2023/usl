package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.api.FunctionEnhancer;
import com.gitee.usl.kernel.plugin.ParameterBinderPlugin;
import com.google.auto.service.AutoService;

/**
 * @author hongda.li
 */
@Order(ParameterBinderEnhancer.PARAM_BINDER_ORDER)
@AutoService(FunctionEnhancer.class)
public class ParameterBinderEnhancer extends AbstractFunctionEnhancer {

    @Description("参数绑定插件生效的优先级")
    public static final int PARAM_BINDER_ORDER = Integer.MIN_VALUE + 10;

    @Override
    protected void enhanceAnnotatedFunction(AnnotatedFunction af) {
        af.plugins().install(Singleton.get(ParameterBinderPlugin.class));
    }

}

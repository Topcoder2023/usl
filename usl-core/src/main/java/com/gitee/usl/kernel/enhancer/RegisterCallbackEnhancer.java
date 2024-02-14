package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.Definable;
import com.gitee.usl.api.RegisterCallback;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.engine.FunctionDefinition;

import java.util.HashSet;
import java.util.Set;

/**
 * 注册回调逻辑增强器
 * 在所有函数注册完成后，获取实现了回调接口的实例，并执行其回调方法
 *
 * @author hongda.li
 */
public class RegisterCallbackEnhancer extends AbstractFunctionEnhancer {
    private final Set<Class<?>> CALLBACK = new HashSet<>(NumberConstant.COMMON_SIZE);

    @Override
    protected void enhanceDefinableFunction(Definable def) {
        // 函数的定义信息
        FunctionDefinition definition = def.definition();

        // 函数所在类的类型
        Class<?> targetType = definition.getMethodMeta().targetType();

        if (definition.getMethodMeta().target() instanceof RegisterCallback rcb && !CALLBACK.contains(targetType)) {
            CALLBACK.add(targetType);
            rcb.callback(definition.getRunner().getConfiguration());
        }
    }
}

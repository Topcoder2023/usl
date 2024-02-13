package com.gitee.usl.kernel.enhancer;

import com.gitee.usl.api.Definable;
import com.gitee.usl.api.RegisterCallback;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.kernel.engine.USLConfiguration;

/**
 * 注册回调逻辑增强器
 * 在所有函数注册完成后，获取实现了回调接口的实例，并执行其回调方法
 *
 * @author hongda.li
 */
public class RegisterCallbackEnhancer extends AbstractFunctionEnhancer {
    @Override
    protected void enhanceDefinableFunction(Definable def) {
        FunctionDefinition definition = def.definition();
        if (definition.getMethodMeta().target() instanceof RegisterCallback rcb) {
            rcb.callback(definition.getRunner().getConfiguration());
        }
    }
}

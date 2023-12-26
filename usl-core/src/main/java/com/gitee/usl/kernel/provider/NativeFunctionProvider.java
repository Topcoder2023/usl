package com.gitee.usl.kernel.provider;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.api.FunctionProvider;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
@AutoService(FunctionProvider.class)
public class NativeFunctionProvider extends AbstractFunctionProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner) {
        AviatorFunction ifPossible = (AviatorFunction) ReflectUtil.newInstanceIfPossible(clz);

        if (ifPossible == null) {
            logger.warn("Unable to instantiate the aviator native class. - {}", clz.getName());
            logger.warn("Consider providing a parameterless constructor or customizing UslFunctionLoader");

            return Collections.emptyList();
        }

        String name = ifPossible.getName();

        if (name == null) {
            return Collections.emptyList();
        }

        FunctionDefinition definition = new FunctionDefinition(name, runner);
        definition.setMethodMeta(new MethodMeta<>(ifPossible, clz, null));

        return Collections.singletonList(definition);
    }

    @Override
    protected AviatorFunction definition2Func(FunctionDefinition definition) {
        // 创建 USL-Aviator 代理函数
        // 动态代理 AviatorFunction 接口中的 call() 方法
        return new NativeFunction(definition, definition.methodMeta().getTarget()).createProxy();
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return AviatorFunction.class.isAssignableFrom(clz)
                && ClassUtil.isNormalClass(clz)
                // 跳过基于注解的函数
                && !AnnotatedFunction.class.isAssignableFrom(clz);
    }
}

package com.gitee.usl.kernel.loader;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.Runner;
import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.NativeFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.grammar.runtime.type._Function;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * @author hongda.li
 */
@Slf4j
public class NativeFunctionLoader extends AbstractFunctionLoader {

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz, Runner runner) {
        _Function ifPossible = (_Function) ReflectUtil.newInstanceIfPossible(clz);

        if (ifPossible == null) {
            log.warn("无法实例化指定类 - {}", clz.getName());
            log.warn("请考虑提供无参构造器或自定义函数加载器");

            return Collections.emptyList();
        }

        String name = ifPossible.name();

        if (name == null) {
            return Collections.emptyList();
        }

        MethodMeta<?> methodMeta = new MethodMeta<>(ifPossible, clz, null);
        FunctionDefinition definition = new FunctionDefinition(name, runner, methodMeta);

        return Collections.singletonList(definition);
    }

    @Override
    protected _Function definition2Func(FunctionDefinition definition) {
        return new NativeFunction(definition, definition.getMethodMeta().target()).createProxy();
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return _Function.class.isAssignableFrom(clz)
                && ClassUtil.isNormalClass(clz)
                && !AnnotatedFunction.class.isAssignableFrom(clz)
                && !AnnotationUtil.hasAnnotation(clz, SystemFunction.class);
    }

}

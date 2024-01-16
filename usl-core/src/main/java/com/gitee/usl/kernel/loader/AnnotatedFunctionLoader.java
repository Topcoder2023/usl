package com.gitee.usl.kernel.loader;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.grammar.runtime.type.Function;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@Slf4j
@Description("基于注解的函数加载器")
public class AnnotatedFunctionLoader extends AbstractFunctionLoader {

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner) {
        Object ifPossible = ReflectUtil.newInstanceIfPossible(clz);

        if (ifPossible == null) {
            log.warn("无法实例化指定类 - {}", clz.getName());
            log.warn("请考虑提供无参构造器或自定义函数加载器");

            return Collections.emptyList();
        }

        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, com.gitee.usl.api.annotation.Function.class)))
                .map(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, com.gitee.usl.api.annotation.Function.class);

                    MethodMeta<?> methodMeta = new MethodMeta<>(ifPossible, clz, method);

                    if (ArrayUtil.isEmpty(accept)) {
                        return new FunctionDefinition(method.getName(), runner, methodMeta);
                    } else {
                        String firstName = accept[NumberConstant.ZERO];
                        FunctionDefinition definition = new FunctionDefinition(firstName, runner, methodMeta);
                        definition.addAlias(accept);
                        return definition;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    protected Function definition2Func(FunctionDefinition definition) {
        return new AnnotatedFunction(definition);
    }

    @Override
    @Description("过滤出声明式函数类")
    protected boolean filter(Class<?> clz) {
        return AnnotationUtil.hasAnnotation(clz, FunctionGroup.class) && ClassUtil.isNormalClass(clz);
    }

}

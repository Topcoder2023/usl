package com.gitee.usl.kernel.engine.provider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.proxy.UslInvocation;
import com.gitee.usl.kernel.engine.UslFunctionDefinition;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
public class PackageUslFunctionProvider extends AbstractUslFunctionProvider {

    @Override
    protected List<UslFunctionDefinition> class2Definition(Class<?> clz) {
        Object ifPossible = ReflectUtil.newInstanceIfPossible(clz);

        if (ifPossible == null) {
            return Collections.emptyList();
        }

        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, Func.class)))
                .flatMap(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, Func.class);

                    if (accept == null) {
                        // 未指定函数名称，则取方法名的首字母小写
                        return Stream.of(this.buildDefinition(CharSequenceUtil.lowerFirst(method.getName()), ifPossible, method));
                    } else {
                        // 指定了函数名称，则取指定的函数名称
                        return Stream.of(accept).map(name -> this.buildDefinition(name, ifPossible, method));
                    }
                })
                .toList();
    }

    /**
     * 封装函数定义信息
     *
     * @param name     函数名称
     * @param instance 函数所在的实例
     * @param method   函数对应的方法
     * @return 函数定义信息
     */
    private UslFunctionDefinition buildDefinition(String name, Object instance, Method method) {
        UslFunctionDefinition definition = new UslFunctionDefinition(name);
        return definition.setInvocation(new UslInvocation<>(instance, instance.getClass(), method, null));
    }

    /**
     * 过滤出声明式函数类
     * 即类上必须有 @Func 注解
     *
     * @param clz 待过滤的类
     * @return 是否符合声明式函数的格式
     */
    @Override
    protected boolean filter(Class<?> clz) {
        return AnnotationUtil.hasAnnotation(clz, Func.class);
    }
}

package com.gitee.usl.kernel.provider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.FunctionProvider;
import com.gitee.usl.api.annotation.FuncClient;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.kernel.engine.ClientFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@AutoService(FunctionProvider.class)
public class ClientFunctionProvider extends AbstractFunctionProvider {
    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz) {
        return Arrays.stream(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, FuncClient.class)))
                .flatMap(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, FuncClient.class);

                    if (accept == null) {
                        // 未指定函数名称，则取方法名转下划线
                        String defaultName = CharSequenceUtil.toUnderlineCase(method.getName());
                        return Stream.of(this.buildDefinition(defaultName, clz, method));
                    } else {
                        // 指定了函数名称，则取指定的函数名称
                        return Stream.of(accept).map(name -> this.buildDefinition(name, clz, method));
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    protected AviatorFunction definition2Func(FunctionDefinition definition) {
        return new ClientFunction(definition);
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return clz.isInterface() && AnnotationUtil.hasAnnotation(clz, FuncClient.class);
    }

    /**
     * 封装函数定义信息
     *
     * @param name   函数名称
     * @param clazz  函数类型
     * @param method 函数对应的方法
     * @return 函数定义信息
     */
    protected FunctionDefinition buildDefinition(String name, Class<?> clazz, Method method) {
        FunctionDefinition definition = new FunctionDefinition(name);
        return definition.setMethodMeta(new MethodMeta<>(null, clazz, method));
    }
}

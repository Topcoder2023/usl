package com.gitee.usl.kernel.engine.provider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.proxy.Invocation;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 注解函数提供者
 * 根据配置类中的包路径信息
 * 扫描包路径下的所有类文件并过滤出带有 @Func 注解的类
 * 将过滤出来的类转为函数定义信息
 * 最后将函数定义信息转为函数实例
 *
 * @author hongda.li
 */
public class AnnotatedFunctionProvider extends AbstractFunctionProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz) {
        Object ifPossible = ReflectUtil.newInstanceIfPossible(clz);

        if (ifPossible == null) {
            logger.warn("Unable to instantiate the function class. - {}", clz.getName());
            logger.warn("Consider providing a parameterless constructor or customizing UslFunctionLoader");

            return Collections.emptyList();
        }

        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, Func.class)))
                .flatMap(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, Func.class);

                    if (accept == null) {
                        // 未指定函数名称，则取方法名转下划线
                        String defaultName = CharSequenceUtil.toUnderlineCase(method.getName());
                        return Stream.of(this.buildDefinition(defaultName, ifPossible, method));
                    } else {
                        // 指定了函数名称，则取指定的函数名称
                        return Stream.of(accept).map(name -> this.buildDefinition(name, ifPossible, method));
                    }
                })
                .toList();
    }

    @Override
    protected AviatorFunction definition2Func(FunctionDefinition definition) {
        return new AnnotatedFunction(definition);
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
        return AnnotationUtil.hasAnnotation(clz, Func.class) && ClassUtil.isNormalClass(clz);
    }

    /**
     * 封装函数定义信息
     *
     * @param name     函数名称
     * @param instance 函数所在的实例
     * @param method   函数对应的方法
     * @return 函数定义信息
     */
    private FunctionDefinition buildDefinition(String name, Object instance, Method method) {
        FunctionDefinition definition = new FunctionDefinition(name);
        return definition.setInvocation(new Invocation<>(instance, instance.getClass(), method, null));
    }
}

package com.gitee.usl.kernel.provider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.kernel.engine.AnnotatedFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.gitee.usl.api.FunctionProvider;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
@AutoService(FunctionProvider.class)
public class AnnotatedFunctionProvider extends AbstractFunctionProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner) {
        Object ifPossible = ReflectUtil.newInstanceIfPossible(clz);

        if (ifPossible == null) {
            logger.warn("Unable to instantiate the function class. - {}", clz.getName());
            logger.warn("Consider providing a parameterless constructor or customizing UslFunctionLoader");

            return Collections.emptyList();
        }

        Func func = AnnotationUtil.getAnnotation(clz, Func.class);
        String prefix = ArrayUtil.isNotEmpty(func.value()) && func.value().length == NumberConstant.ONE
                ? func.value()[NumberConstant.ZERO]
                : null;

        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, Func.class)))
                .map(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, Func.class);

                    if (ArrayUtil.isEmpty(accept)) {
                        // 未指定函数名称，则取方法名
                        String defaultName = CharSequenceUtil.addPrefixIfNot(method.getName(), prefix);
                        return new FunctionDefinition(defaultName, runner).setMethodMeta(new MethodMeta<>(ifPossible, clz, method));
                    } else {
                        // 指定了函数名称，则取指定的函数名称
                        String firstName = CharSequenceUtil.addPrefixIfNot(accept[NumberConstant.ZERO], prefix);
                        FunctionDefinition definition = new FunctionDefinition(firstName, runner).setMethodMeta(new MethodMeta<>(ifPossible, clz, method));
                        if (accept.length > NumberConstant.ONE) {
                            definition.addAlias(prefix, ArrayUtil.sub(accept, NumberConstant.ONE, accept.length));
                        }
                        return definition;
                    }
                })
                .collect(Collectors.toList());
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
}

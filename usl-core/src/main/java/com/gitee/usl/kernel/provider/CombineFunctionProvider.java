package com.gitee.usl.kernel.provider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.FunctionProvider;
import com.gitee.usl.api.annotation.CombineFunc;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.api.annotation.Param;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.infra.structure.StringList;
import com.gitee.usl.kernel.engine.CombineFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@AutoService(FunctionProvider.class)
public class CombineFunctionProvider extends AbstractFunctionProvider {

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz) {
        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, Func.class)
                        && AnnotationUtil.hasAnnotation(method, CombineFunc.class)))
                .flatMap(method -> {
                    CombineFunc combine = AnnotationUtil.getAnnotation(method, CombineFunc.class);
                    String script = combine.value();
                    USLRunner runner = USLRunner.findRunnerByName(combine.runnerName());

                    Assert.notBlank(script, "Script content can not be blank.");
                    Assert.notNull(runner, "The specified actuator can not be found - " + combine.runnerName());

                    StringList names = this.resolveName(method.getParameters());

                    String[] accept = AnnotationUtil.getAnnotationValue(method, Func.class);

                    if (accept == null) {
                        // 未指定函数名称，则取方法名转下划线
                        String defaultName = CharSequenceUtil.toUnderlineCase(method.getName());
                        return Stream.of(this.buildDefinition(defaultName, clz, method, script, runner, names));
                    } else {
                        // 指定了函数名称，则取指定的函数名称
                        return Stream.of(accept).map(name -> this.buildDefinition(name, clz, method, script, runner, names));
                    }
                })
                .collect(Collectors.toList());
    }

    private StringList resolveName(Parameter[] parameters) {
        if (ArrayUtil.isEmpty(parameters)) {
            return StringList.empty();
        }

        return new StringList(Stream.of(parameters)
                .map(parameter -> AnnotationUtil.getAnnotation(parameter, Param.class))
                .map(Param::value)
                .collect(Collectors.toList()));
    }

    @Override
    protected AviatorFunction definition2Func(FunctionDefinition definition) {
        return new CombineFunction(definition);
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return clz.isInterface() && AnnotationUtil.hasAnnotation(clz, Func.class);
    }

    /**
     * 封装函数定义信息
     *
     * @param name   函数名称
     * @param clz    函数对应的类
     * @param method 函数对应的方法
     * @param script 脚本内容
     * @param runner 执行器实例
     * @return 函数定义信息
     */
    protected FunctionDefinition buildDefinition(String name,
                                                 Class<?> clz,
                                                 Method method,
                                                 String script,
                                                 USLRunner runner,
                                                 StringList names) {
        FunctionDefinition definition = new FunctionDefinition(name);
        AttributeMeta attribute = definition.attribute();
        attribute.insert(StringConstant.SCRIPT_NAME, script);
        attribute.insert(StringConstant.RUNNER_NAME, runner);
        attribute.insert(StringConstant.PARAMS_NAME, names);
        return definition.setMethodMeta(new MethodMeta<>(null, clz, method));
    }
}

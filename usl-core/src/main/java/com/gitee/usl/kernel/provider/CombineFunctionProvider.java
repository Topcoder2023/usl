package com.gitee.usl.kernel.provider;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.FunctionProvider;
import com.gitee.usl.api.annotation.*;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.enums.EngineName;
import com.gitee.usl.infra.exception.UslException;
import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;
import com.gitee.usl.infra.structure.StringList;
import com.gitee.usl.kernel.engine.CombineFunction;
import com.gitee.usl.kernel.engine.FunctionDefinition;
import com.google.auto.service.AutoService;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import javax.script.*;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@AutoService(FunctionProvider.class)
public class CombineFunctionProvider extends AbstractFunctionProvider {
    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner) {
        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, Function.class)
                        && AnnotationUtil.hasAnnotation(method, Combine.class)))
                .map(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, Function.class);

                    if (accept == null) {
                        // 未指定函数名称，则取方法名
                        return this.toDef(method.getName(), clz, method, runner);
                    } else {
                        // 指定了函数名称，则取指定的函数名称
                        String firstName = accept[NumberConstant.ZERO];
                        FunctionDefinition definition = this.toDef(firstName, clz, method, runner);
                        definition.addAlias(ArrayUtil.sub(accept, NumberConstant.ONE, accept.length));
                        return definition;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    protected AviatorFunction definition2Func(FunctionDefinition definition) {
        return new CombineFunction(definition);
    }

    @Override
    protected boolean filter(Class<?> clz) {
        return clz.isInterface() && AnnotationUtil.hasAnnotation(clz, FunctionGroup.class);
    }

    /**
     * 封装函数定义信息
     *
     * @param name   函数名称
     * @param clz    函数对应的类
     * @param method 函数对应的方法
     * @param runner 当前使用的 USL 实例
     * @return 函数定义信息
     */
    protected FunctionDefinition toDef(String name, Class<?> clz, Method method, USLRunner runner) {
        // 获取组合函数注解以及注解中的脚本内容
        Combine combine = AnnotationUtil.getAnnotation(method, Combine.class);
        String script = combine.value();

        // 获取脚本引擎注解，可能为空
        EngineName engineName = AnnotationUtil.getAnnotationValue(method, Engine.class);
        // 获取USL执行器实例，可能为空
        USLRunner found = USLRunner.findRunnerByName(combine.runnerName());

        // 校验脚本
        Assert.notBlank(script, "Script content can not be blank.");

        // 解析参数名称
        StringList names = this.resolveName(method.getParameters());

        // 构造函数定义信息
        FunctionDefinition definition = new FunctionDefinition(name, runner);
        AttributeMeta attribute = definition.attribute();
        attribute.put(StringConstant.SCRIPT_NAME, script);
        attribute.put(StringConstant.RUNNER_NAME, found);
        attribute.put(StringConstant.PARAMS_NAME, names);

        // 如果指定了引擎名称，则获取并编译脚本
        if (engineName != null) {
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(engineName.getName());
            Assert.notNull(scriptEngine, "The specified script engine can not be found - {}", engineName.getName());
            try {
                CompiledScript compiledScript = ((Compilable) scriptEngine).compile(script);
                attribute.put(StringConstant.COMPILED_SCRIPT, compiledScript);
            } catch (ScriptException e) {
                throw new UslException(e);
            }
        }

        return definition.setMethodMeta(new MethodMeta<>(null, clz, method));
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
}

package com.gitee.usl.kernel.loader;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.FunctionLoader;
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
@AutoService(FunctionLoader.class)
public class CombineFunctionLoader extends AbstractFunctionLoader {
    private final ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    @Override
    protected List<FunctionDefinition> class2Definition(Class<?> clz, USLRunner runner) {
        return Stream.of(ReflectUtil.getMethods(clz, method -> AnnotationUtil.hasAnnotation(method, Function.class)
                        && AnnotationUtil.hasAnnotation(method, Combine.class)))
                .map(method -> {
                    String[] accept = AnnotationUtil.getAnnotationValue(method, Function.class);

                    if (accept == null) {
                        return this.toDefinition(method.getName(), clz, method, runner);
                    } else {
                        String firstName = accept[NumberConstant.ZERO];
                        FunctionDefinition definition = this.toDefinition(firstName, clz, method, runner);
                        definition.addAlias(accept);
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

    @Description("封装函数定义信息")
    protected FunctionDefinition toDefinition(String name, Class<?> clz, Method method, USLRunner runner) {

        @Description("组合函数注解")
        Combine combine = AnnotationUtil.getAnnotation(method, Combine.class);

        @Description("组合函数注解中的脚本内容")
        String script = combine.value();

        @Description("脚本引擎注解")
        EngineName engineName = AnnotationUtil.getAnnotationValue(method, Engine.class);

        Assert.notBlank(script, "脚本内容不能为空");

        @Description("参数名称")
        StringList names = this.resolveName(method.getParameters());

        @Description("函数定义信息")
        FunctionDefinition definition = new FunctionDefinition(name, runner);
        AttributeMeta attribute = definition.getAttribute();
        attribute.put(StringConstant.SCRIPT_NAME, script);
        attribute.put(StringConstant.RUNNER_NAME, runner);
        attribute.put(StringConstant.PARAMS_NAME, names);

        if (engineName != null) {
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByName(engineName.getName());
            Assert.notNull(scriptEngine, "无法加载指定的脚本引擎 - {}", engineName.getName());
            try {
                @Description("编译脚本结果")
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

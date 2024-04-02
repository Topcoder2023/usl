package com.manda.usl.kernel.function;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.manda.usl.kernel.annotation.Function;
import com.manda.usl.kernel.annotation.FunctionGroup;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.BeanId;
import com.manda.usl.kernel.common.FunctionEnhancer;
import com.manda.usl.kernel.entity.FunctionMeta;
import com.manda.usl.kernel.entity.MethodMeta;
import com.manda.usl.kernel.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@Slf4j
public class EnhancedFunctionLoader implements FunctionLoader {
    private final String runnerName;
    private final Map<String, AviatorFunction> functions = new HashMap<>();
    private List<FunctionEnhancer> enhancerList;

    public EnhancedFunctionLoader(String runnerName) {
        this.runnerName = runnerName;
        this.parse();
    }

    @Override
    public AviatorFunction onFunctionNotFound(String name) {
        return functions.get(name);
    }

    private void parse() {
        BeanContext.find(this::filter).forEach(bean -> Stream.of(ReflectUtil.getMethods(bean.getClass(), this::filter))
                .map(method -> {
                    FunctionMeta functionMeta = new FunctionMeta(this.parseName(method), new MethodMeta(bean, method));
                    functionMeta.getPluginList().addAll(this.parsePlugin(bean.getClass(), method));
                    return functionMeta;
                })
                .map(EnhancedFunction::new)
                .map(this::enhance)
                .forEach(this::cache));
    }

    private EnhancedFunction enhance(EnhancedFunction function) {
        if (enhancerList == null) {
            enhancerList = BeanContext.find(FunctionEnhancer.class);
        }
        if (CollUtil.isEmpty(enhancerList)) {
            return function;
        }
        enhancerList.forEach(enhancer -> enhancer.enhance(function));
        return function;
    }

    private List<Plugin> parsePlugin(Class<?> clazz, Method method) {
        List<Plugin> pluginList = new ArrayList<>();
        FunctionGroup pluginGroup = AnnotationUtil.getAnnotation(clazz, FunctionGroup.class);
        this.parsePlugin(pluginGroup.plugins(), pluginList);
        Function plugin = AnnotationUtil.getAnnotation(method, Function.class);
        this.parsePlugin(plugin.plugins(), pluginList);
        return pluginList;
    }

    private void parsePlugin(Class<?>[] plugins, List<Plugin> container) {
        if (ArrayUtil.isEmpty(plugins)) {
            return;
        }
        Arrays.stream(plugins)
                .map(BeanContext::findFirst)
                .filter(Objects::nonNull)
                .map(Plugin.class::cast)
                .forEach(container::add);
    }

    private void cache(EnhancedFunction function) {
        String name = function.getName();
        if (this.functions.containsKey(name)) {
            log.warn("The function with the same name already exists : {}", name);
        } else {
            this.functions.put(name, function);
            log.debug("Register function : {}", name);
        }
    }

    private boolean filter(Method method) {
        return AnnotationUtil.hasAnnotation(method, Function.class);
    }

    private boolean filter(BeanId beanId) {
        return Objects.equals(runnerName, AnnotationUtil.getAnnotationValue(beanId.getType(), FunctionGroup.class));
    }

    private String parseName(Method method) {
        String name = AnnotationUtil.getAnnotationValue(method, Function.class);
        return CharSequenceUtil.isBlank(name) ? method.getName() : name;
    }
}

package com.manda.usl.kernel.function;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.manda.usl.kernel.annotation.Func;
import com.manda.usl.kernel.annotation.FuncGroup;
import com.manda.usl.kernel.common.FuncGroupScanner;
import com.manda.usl.kernel.common.FuncEnhancer;
import com.manda.usl.kernel.view.FunctionMeta;
import com.manda.usl.kernel.view.MethodMeta;
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

    /**
     * 当前执行器的名称
     */
    private final String runnerName;


    /**
     * 函数增强器集合
     */
    private final List<FuncEnhancer> enhancerList;

    /**
     * 函数缓存
     */
    private final Map<String, AviatorFunction> functions = new HashMap<>();

    public EnhancedFunctionLoader(String runnerName, List<FuncEnhancer> enhancerList) {
        this.runnerName = runnerName;
        this.enhancerList = enhancerList;
        this.parse();
    }

    @Override
    public AviatorFunction onFunctionNotFound(String name) {
        return functions.get(name);
    }

    private void parse() {
        List<FuncGroupScanner> scannerList = ServiceLoaderUtil.loadList(FuncGroupScanner.class);

        FuncGroupScanner.Scanner scanner = new FuncGroupScanner.Scanner(this.runnerName);

        for (FuncGroupScanner groupScanner : scannerList) {
            groupScanner.doScan(scanner);
        }

        scanner.getGroupList()
                .forEach(group -> Stream.of(ReflectUtil.getMethods(group.getClass(), this::filter))
                        .map(method -> {
                            FunctionMeta functionMeta = new FunctionMeta(this.parseName(method), new MethodMeta(group, method));
                            functionMeta.getPluginList().addAll(this.parsePlugin(group.getClass(), method));
                            return functionMeta;
                        })
                        .map(EnhancedFunction::new)
                        .map(this::enhance)
                        .forEach(this::cache));
    }

    private EnhancedFunction enhance(EnhancedFunction function) {
        enhancerList.forEach(enhancer -> enhancer.enhance(function));
        return function;
    }

    private List<Plugin> parsePlugin(Class<?> clazz, Method method) {
        List<Plugin> pluginList = new ArrayList<>();

        FuncGroup pluginGroup = AnnotationUtil.getAnnotation(clazz, FuncGroup.class);
        this.parsePlugin(pluginGroup.plugins(), pluginList);

        Func plugin = AnnotationUtil.getAnnotation(method, Func.class);
        this.parsePlugin(plugin.plugins(), pluginList);

        return pluginList;
    }

    private void parsePlugin(Class<?>[] plugins, List<Plugin> container) {
        if (ArrayUtil.isEmpty(plugins)) {
            return;
        }

        Arrays.stream(plugins)
                .map(Singleton::get)
                .filter(Objects::nonNull)
                .map(Plugin.class::cast)
                .forEach(ins -> {
                    if (!container.contains(ins)) {
                        container.add(ins);
                    }
                });
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
        return AnnotationUtil.hasAnnotation(method, Func.class);
    }

    private String parseName(Method method) {
        String name = AnnotationUtil.getAnnotationValue(method, Func.class);
        return CharSequenceUtil.isBlank(name) ? method.getName() : name;
    }
}

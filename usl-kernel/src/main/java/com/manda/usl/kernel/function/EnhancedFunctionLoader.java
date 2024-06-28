package com.manda.usl.kernel.function;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Singleton;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.ServiceLoaderUtil;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.manda.usl.kernel.annotation.Func;
import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.common.FuncScanner;
import com.manda.usl.kernel.common.FuncEnhancer;
import com.manda.usl.kernel.view.FuncMeta;
import com.manda.usl.kernel.view.MethodMeta;
import com.manda.usl.kernel.plugin.Plugin;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
        // 获取所有函数组扫描器
        List<FuncScanner> scannerList = ServiceLoaderUtil.loadList(FuncScanner.class);

        // 实例化适用于当前执行器的扫描器
        FuncScanner.Scanner scanner = new FuncScanner.Scanner(this.runnerName);

        // 执行扫描
        for (FuncScanner groupScanner : scannerList) {
            groupScanner.doScan(scanner);
        }

        // 将扫描结果转为函数组实例
        scanner.getGroupList()
                .forEach(group -> Stream.of(ReflectUtil.getMethods(group.getClass(), this::matchFuncMethod))
                        .map(method -> {
                            FuncMeta funcMeta = new FuncMeta(this.getFuncName(method), new MethodMeta(group, method));
                            funcMeta.addPluginList(this.buildPluginList(group.getClass(), method));
                            return funcMeta;
                        })
                        .map(EnhancedFunction::new)
                        .map(this::enhance)
                        .forEach(this::cache));
    }

    private EnhancedFunction enhance(EnhancedFunction function) {
        enhancerList.forEach(enhancer -> {
            try {
                enhancer.enhance(function);
            } catch (Exception e) {
                throw new FuncException("Exception occurred in function enhancer", e);
            }
        });
        return function;
    }

    private List<Plugin> buildPluginList(Class<?> type, Method method) {
        return Stream.concat(
                        Stream.of(AnnotationUtil.getAnnotation(type, Func.class).plugins()),
                        Stream.of(AnnotationUtil.getAnnotation(method, Func.class).plugins())
                )
                .distinct()
                .map(Singleton::get)
                .filter(Objects::nonNull)
                .map(Plugin.class::cast)
                .sorted(Comparator.comparingInt(Plugin::order))
                .toList();
    }

    private void cache(EnhancedFunction function) {
        String name = function.getName();
        Assert.isFalse(this.functions.containsKey(name), "The function with the same name already exists : {}", name);

        this.functions.put(name, function);
        log.debug("Cache function : {}", name);
    }

    private boolean matchFuncMethod(Method method) {
        return AnnotationUtil.hasAnnotation(method, Func.class)
                && !Modifier.isStatic(method.getModifiers())
                && !Modifier.isPrivate(method.getModifiers());
    }

    private String getFuncName(Method method) {
        return CharSequenceUtil.blankToDefault(
                AnnotationUtil.getAnnotationValue(method, Func.class),
                method.getName()
        );
    }
}

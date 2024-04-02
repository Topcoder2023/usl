package com.manda.usl.kernel.function;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import com.googlecode.aviator.FunctionLoader;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.manda.usl.kernel.annotation.Function;
import com.manda.usl.kernel.annotation.FunctionGroup;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.BeanId;
import com.manda.usl.kernel.entity.FunctionMeta;
import com.manda.usl.kernel.entity.MethodMeta;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author hongda.li
 */
@Slf4j
public class EnhancedFunctionLoader implements FunctionLoader {
    private final String runnerName;
    private final Map<String, AviatorFunction> functions = new HashMap<>();

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
                .map(method -> new FunctionMeta(this.parseName(method), new MethodMeta(bean, method)))
                .map(EnhancedFunction::new)
                .forEach(this::cache));
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

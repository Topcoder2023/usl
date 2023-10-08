package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.proxy.MethodMeta;
import com.gitee.usl.infra.structure.AttributeMeta;

import java.util.*;

/**
 * USL 函数定义
 *
 * @author hongda.li
 */
public class FunctionDefinition {
    private final String name;
    private MethodMeta<?> methodMeta;
    private final AttributeMeta attribute = new AttributeMeta();

    public FunctionDefinition(String name) {
        this.name = name;
    }

    public MethodMeta<?> methodMeta() {
        return methodMeta;
    }

    public FunctionDefinition setMethodMeta(MethodMeta<?> methodMeta) {
        this.methodMeta = methodMeta;
        return this;
    }

    public AttributeMeta attribute() {
        return attribute;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionDefinition.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("methodMeta=" + methodMeta)
                .add("attribute=" + attribute)
                .toString();
    }
}

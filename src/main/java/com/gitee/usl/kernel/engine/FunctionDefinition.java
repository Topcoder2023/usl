package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.proxy.Invocation;

import java.util.Map;
import java.util.StringJoiner;

/**
 * USL 函数定义
 *
 * @author hongda.li
 */
public class FunctionDefinition {
    private final String name;
    private Invocation<?> invocation;
    private Map<String, Object> properties;

    public FunctionDefinition(String name) {
        this.name = name;
    }

    public Invocation<?> getInvocation() {
        return invocation;
    }

    public FunctionDefinition setInvocation(Invocation<?> invocation) {
        this.invocation = invocation;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public FunctionDefinition setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", FunctionDefinition.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("invocation=" + invocation)
                .add("properties=" + properties)
                .toString();
    }
}

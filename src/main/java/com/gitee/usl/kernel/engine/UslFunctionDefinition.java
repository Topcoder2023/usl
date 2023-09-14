package com.gitee.usl.kernel.engine;

import com.gitee.usl.infra.proxy.UslInvocation;

import java.util.Map;
import java.util.StringJoiner;

/**
 * USL 函数定义
 *
 * @author hongda.li
 */
public class UslFunctionDefinition {
    private final String name;
    private UslInvocation<?> invocation;
    private Map<String, Object> properties;

    public UslFunctionDefinition(String name) {
        this.name = name;
    }

    public UslInvocation<?> getInvocation() {
        return invocation;
    }

    public UslFunctionDefinition setInvocation(UslInvocation<?> invocation) {
        this.invocation = invocation;
        return this;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public UslFunctionDefinition setProperties(Map<String, Object> properties) {
        this.properties = properties;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UslFunctionDefinition.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("invocation=" + invocation)
                .add("properties=" + properties)
                .toString();
    }
}

package com.gitee.usl.plugin.impl.sensitive;

import com.gitee.usl.infra.design.IfContext;

import java.util.StringJoiner;

/**
 * @author hongda.li
 */
public class SensitiveContext extends IfContext<Object> {
    private final SensitiveType type;
    private String result;

    public SensitiveContext(Object value, SensitiveType type) {
        super(value);
        this.type = type;
    }

    public SensitiveType getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public SensitiveContext setResult(String result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SensitiveContext.class.getSimpleName() + "[", "]")
                .add("type=" + type)
                .add("result='" + result + "'")
                .toString();
    }
}

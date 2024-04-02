package com.manda.usl.kernel.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class Param {

    private final boolean cached;

    private final String expression;

    private final Map<String, Object> context;

    public Param(String expression) {
        this(true, expression, new HashMap<>());
    }

    public Param(boolean cached, String expression, Map<String, Object> context) {
        this.cached = cached;
        this.context = context;
        this.expression = expression;
    }

    public Param addContext(String name, Object value) {
        this.context.put(name, value);
        return this;
    }

    public Param addContext(Map<String, Object> other) {
        this.context.putAll(other);
        return this;
    }

}

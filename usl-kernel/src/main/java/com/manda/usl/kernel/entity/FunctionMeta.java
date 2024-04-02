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
public class FunctionMeta {

    private final String name;

    private MethodMeta methodMeta;

    private final Map<String, Object> attribute = new HashMap<>();

    public FunctionMeta(String name, MethodMeta methodMeta) {
        this.name = name;
        this.methodMeta = methodMeta;
    }

}

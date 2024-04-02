package com.manda.usl.kernel.entity;

import com.manda.usl.kernel.plugin.Plugin;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@Data
@Accessors(chain = true)
public class FunctionMeta {

    private final String name;

    private MethodMeta methodMeta;

    private final List<Plugin> pluginList = new ArrayList<>();

    private final Map<String, Object> attribute = new HashMap<>();

    public FunctionMeta(String name, MethodMeta methodMeta) {
        this.name = name;
        this.methodMeta = methodMeta;
    }

}

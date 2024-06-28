package com.manda.usl.kernel.view;

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
public class FuncMeta {

    private final String name;

    private MethodMeta methodMeta;

    private final List<Plugin> pluginList = new ArrayList<>();

    private final Map<String, Object> attribute = new HashMap<>();

    public FuncMeta(String name, MethodMeta methodMeta) {
        this.name = name;
        this.methodMeta = methodMeta;
    }

    public void addPlugin(Plugin plugin) {
        this.pluginList.add(plugin);
    }

    public void addAttribute(String name, Object value) {
        this.attribute.put(name, value);
    }

    public void addPluginList(List<Plugin> pluginList) {
        this.pluginList.addAll(pluginList);
    }
}

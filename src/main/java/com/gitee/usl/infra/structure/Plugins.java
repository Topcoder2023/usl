package com.gitee.usl.infra.structure;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.kernel.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author hongda.li
 */
public class Plugins {
    private final List<Plugin> container;

    public Plugins() {
        container = new ArrayList<>(NumberConstant.COMMON_SIZE);
    }

    public List<Plugin> asList() {
        return container;
    }

    public void install(Plugin plugin) {
        Assert.notNull(plugin);
        this.container.add(plugin);
    }

    /**
     * 执行插件
     *
     * @param pluginType 插件类型
     * @param consumer   插件消费者
     * @param <T>        插件泛型
     */
    public <T> void makePlugin(Class<T> pluginType, Consumer<T> consumer) {
        this.container.stream()
                .filter(plugin -> pluginType.isAssignableFrom(plugin.getClass()))
                .map(pluginType::cast)
                .forEach(consumer);
    }
}

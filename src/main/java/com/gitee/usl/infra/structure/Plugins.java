package com.gitee.usl.infra.structure;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.api.plugin.Plugin;

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

    /**
     * 安装插件
     *
     * @param plugin 插件
     */
    public void install(Plugin plugin) {
        Assert.notNull(plugin);
        this.container.add(plugin);
    }

    /**
     * 从指定位置安装插件
     *
     * @param index  插件位置
     * @param plugin 插件
     */
    public void install(int index, Plugin plugin) {
        this.container.add(index, plugin);
    }

    /**
     * 卸载指定类型的插件
     *
     * @param type 插件类型
     */
    public void uninstall(Class<? extends Plugin> type) {
        this.container.removeIf(plugin -> type.isAssignableFrom(plugin.getClass()));
    }

    /**
     * 卸载全部插件
     */
    public void uninstall() {
        this.container.clear();
    }

    /**
     * 遍历所有插件
     *
     * @param consumer 插件消费者
     */
    public void visit(Consumer<Plugin> consumer) {
        this.container.forEach(consumer);
    }

    /**
     * 获取插件总数量
     *
     * @return 插件数量
     */
    public int size() {
        return this.container.size();
    }

    /**
     * 执行插件
     *
     * @param pluginType 插件类型
     * @param consumer   插件消费者
     * @param <T>        插件泛型
     */
    public <T> void execute(Class<T> pluginType, Consumer<T> consumer) {
        this.container.stream()
                .filter(plugin -> pluginType.isAssignableFrom(plugin.getClass()))
                .map(pluginType::cast)
                .forEach(consumer);
    }
}

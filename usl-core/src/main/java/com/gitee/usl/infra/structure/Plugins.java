package com.gitee.usl.infra.structure;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.api.annotation.Description;
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
        container = new ArrayList<>(NumberConstant.EIGHT);
    }

    @Description("安装插件")
    public void install(Plugin plugin) {
        Assert.notNull(plugin);
        boolean exists = this.container.stream().anyMatch(item -> item.getClass().equals(plugin.getClass()));
        if (!exists) {
            this.container.add(plugin);
        }
    }

    @Description("从指定位置安装插件")
    public void install(int index, Plugin plugin) {
        this.container.add(index, plugin);
    }


    @Description("卸载指定类型的插件")
    public void uninstall(Class<? extends Plugin> type) {
        this.container.removeIf(plugin -> type.isAssignableFrom(plugin.getClass()));
    }

    @Description("卸载全部插件")
    public void uninstall() {
        this.container.clear();
    }

    @Description("遍历所有插件")
    public void visit(Consumer<Plugin> consumer) {
        this.container.forEach(consumer);
    }

    @Description("获取插件总数量")
    public int size() {
        return this.container.size();
    }

    @Description("执行插件")
    public <T> void execute(Class<T> pluginType, Consumer<T> consumer) {
        this.container.stream()
                .filter(plugin -> pluginType.isAssignableFrom(plugin.getClass()))
                .map(pluginType::cast)
                .forEach(consumer);
    }

}

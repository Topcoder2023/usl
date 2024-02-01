package com.gitee.usl.infra.structure;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.infra.utils.AnnotatedComparator;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author hongda.li
 */
@Description("插件链")
public class Plugins {

    @Description("插件容器")
    private final List<Plugin> container = new UniqueList<>(NumberConstant.EIGHT);

    @Description("安装插件")
    public void install(Plugin plugin) {
        Objects.requireNonNull(plugin);
        this.container.add(plugin);
        this.container.sort(Comparator.comparing(item -> AnnotatedComparator.getOrder(item.getClass())));
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

    public void sort(Comparator<Plugin> comparator) {
        Objects.requireNonNull(comparator);
        this.container.sort(comparator);
    }

}

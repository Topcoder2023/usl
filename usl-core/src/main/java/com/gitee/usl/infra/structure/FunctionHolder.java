package com.gitee.usl.infra.structure;

import com.gitee.usl.api.Definable;
import com.gitee.usl.api.Overloaded;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.utils.LambdaHelper;
import com.gitee.usl.grammar.runtime.type._Function;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author hongda.li
 */
@Slf4j
@ToString
@Description("函数实例容器")
public class FunctionHolder {

    @Description("函数别名映射")
    private final Map<String, String> aliasMap;

    @Description("函数名称映射")
    private final Map<String, _Function> container;

    public FunctionHolder() {
        this.aliasMap = new HashMap<>(NumberConstant.NORMAL_SIZE);
        this.container = new HashMap<>(NumberConstant.NORMAL_MAX_SIZE);
    }

    /**
     * 注册函数
     *
     * @param function 函数实例
     */
    public void register(_Function function) {
        String name = function.name();

        _Function found = this.container.get(name);

        if (found != null) {
            // 函数重载标识
            boolean overload = found instanceof Overloaded
                    && function instanceof Overloaded
                    && ((Definable) found).definition().getArgsLength()
                    != ((Definable) function).definition().getArgsLength();

            if (overload) {
                ((Overloaded<?>) found).addOverloadImpl((Overloaded<?>) function);
                log.debug("函数重载 - [{}]", name);
            } else {
                log.warn("重复注册 - [{}]", name);
            }
            return;
        }

        log.debug("注册函数 - [{}]", name);
        this.container.put(name, function);

        if (function instanceof Definable definable) {
            definable.definition()
                    .getAlias()
                    .stream()
                    .filter(item -> !Objects.equals(item, name))
                    .forEach(aliasName -> {
                        this.aliasMap.put(aliasName, name);
                        log.debug("函数别名 - [{} ==> {}]", name, aliasName);
                    });
        }
    }

    @Description("遍历函数")
    public void onVisit(Consumer<_Function> consumer) {
        this.onVisit(LambdaHelper.anyTrue(), consumer);
    }

    @Description("遍历指定函数")
    public void onVisit(Predicate<_Function> predicate, Consumer<_Function> consumer) {
        this.toList().stream().filter(predicate).forEach(consumer);
    }

    @Description("通过函数名称检索函数实例")
    public _Function search(String name) {
        final String key;

        if (!this.container.containsKey(name)) {
            key = this.aliasMap.get(name);
        } else {
            key = name;
        }

        return this.container.get(key);
    }

    @Description("获取所有函数实例")
    public List<_Function> toList() {
        List<_Function> functions = new ArrayList<>(this.container.values());
        this.container.values()
                .stream()
                .filter(item -> item instanceof Overloaded)
                .map(item -> (Overloaded<?>) item)
                .map(Overloaded::allOverloadImpl)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .forEach(functions::add);
        return functions;
    }
}

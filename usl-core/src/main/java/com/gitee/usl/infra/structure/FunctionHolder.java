package com.gitee.usl.infra.structure;

import cn.hutool.core.collection.CollUtil;
import com.gitee.usl.api.Definable;
import com.gitee.usl.api.Overloaded;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.infra.utils.LambdaHelper;
import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author hongda.li
 */
@Slf4j
@Description("函数实例容器")
public class FunctionHolder {

    @Description("函数别名映射")
    private final Map<String, String> aliasMap;

    @Description("函数名称映射")
    private final Map<String, AviatorFunction> container;

    public FunctionHolder() {
        this.aliasMap = new HashMap<>(NumberConstant.EIGHT);
        this.container = new HashMap<>(NumberConstant.COMMON_SIZE);
    }

    @Description("注册函数")
    public void register(AviatorFunction function) {
        String name = function.getName();

        AviatorFunction found = this.container.get(name);

        if (found != null) {

            @Description("函数重载")
            boolean overload = found instanceof Overloaded
                    && function instanceof Overloaded
                    && ((Definable) found).definition().getArgsLength()
                    != ((Definable) function).definition().getArgsLength();

            if (overload) {
                ((Overloaded<?>) found).addOverloadImpl((Overloaded<?>) function);
                log.debug("函数重载 - [{}]", name);
            } else {
                log.warn("函数已被注册 - [{}]", name);
            }

            return;
        }

        log.debug("注册函数 - [{}]", name);
        this.container.put(name, function);
    }

    @Description("注册函数")
    public void register(AviatorFunction function, Set<String> alias) {
        this.register(function);

        if (CollUtil.isEmpty(alias)) {
            return;
        }

        String actualName = function.getName();

        alias.stream().filter(item -> !Objects.equals(item, actualName))
                .forEach(aliasName -> {
                    this.aliasMap.put(aliasName, actualName);
                    log.debug("函数别名 - [{} - {}]", actualName, aliasName);
                });
    }

    @Description("遍历函数")
    public void onVisit(Consumer<AviatorFunction> consumer) {
        this.onVisit(LambdaHelper.anyTrue(), consumer);
    }

    @Description("遍历指定函数")
    public void onVisit(Predicate<AviatorFunction> predicate, Consumer<AviatorFunction> consumer) {
        this.toList().stream().filter(predicate).forEach(consumer);
    }

    @Description("通过函数名称检索函数实例")
    public AviatorFunction search(String name) {
        final String key;

        if (!this.container.containsKey(name)) {
            key = this.aliasMap.get(name);
        } else {
            key = name;
        }

        return this.container.get(key);
    }

    @Description("获取所有函数实例")
    public List<AviatorFunction> toList() {
        List<AviatorFunction> functions = new ArrayList<>(this.container.values());
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

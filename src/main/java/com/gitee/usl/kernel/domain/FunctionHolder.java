package com.gitee.usl.kernel.domain;

import cn.hutool.core.lang.Assert;
import com.gitee.usl.infra.constant.NumberConstant;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 通过组合模式屏蔽 Map 的部分方法
 * 仅暴露注册、检索以及遍历的方法
 *
 * @author hongda.li
 */
public class FunctionHolder {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, AviatorFunction> container;

    public FunctionHolder() {
        this.container = HashMap.newHashMap(NumberConstant.COMMON_SIZE);
    }

    /**
     * 注册函数
     *
     * @param function 非空的函数实例
     */
    public void register(AviatorFunction function) {
        Assert.notNull(function);
        String name = function.getName();

        if (this.container.containsKey(name)) {
            logger.warn("USL function has been registered - [{}]", name);
            return;
        }

        this.container.put(name, function);

        logger.info("Register USL function - [{}]", name);
    }

    /**
     * 遍历函数
     *
     * @param consumer 函数消费者
     */
    public void onVisit(Consumer<AviatorFunction> consumer) {
        this.container.values().forEach(consumer);
    }

    /**
     * 遍历部分函数
     *
     * @param predicate 函数过滤器
     * @param consumer  函数消费者
     */
    public void onVisit(Predicate<AviatorFunction> predicate, Consumer<AviatorFunction> consumer) {
        this.container.values()
                .stream()
                .filter(predicate)
                .forEach(consumer);
    }

    /**
     * 通过函数名称检索函数实例
     *
     * @param name 函数名称
     * @return 函数实例
     */
    public AviatorFunction search(String name) {
        AviatorFunction function = this.container.get(name);

        if (function == null) {
            logger.warn("USL function not found - [{}]", name);
        }

        return function;
    }

    public List<AviatorFunction> toList() {
        return new ArrayList<>(this.container.values());
    }
}

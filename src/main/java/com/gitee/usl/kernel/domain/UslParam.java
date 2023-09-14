package com.gitee.usl.kernel.domain;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * USL 参数结构
 *
 * @author hongda.li
 */
public class UslParam {
    /**
     * 是否缓存表达式
     */
    private boolean cached;

    /**
     * 表达式内容
     */
    private String expression;

    /**
     * 上下文变量
     */
    private Map<String, Object> context;

    public UslParam() {
        this.cached = true;
        this.context = HashMap.newHashMap(NumberConstant.COMMON_SIZE);
    }

    public UslParam setContext(String name, Object value) {
        this.context.put(name, value);
        return this;
    }

    public boolean isCached() {
        return cached;
    }

    public UslParam setCached(boolean cached) {
        this.cached = cached;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public UslParam setExpression(String expression) {
        this.expression = expression;
        return this;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public UslParam setContext(Map<String, Object> context) {
        this.context = context;
        return this;
    }

    @Override
    public String toString() {
        return "UslParam{" +
                "cached=" + cached +
                ", expression='" + expression + '\'' +
                ", context=" + context +
                '}';
    }
}

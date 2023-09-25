package com.gitee.usl.kernel.domain;

import com.gitee.usl.infra.constant.NumberConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * USL 参数结构
 *
 * @author hongda.li
 */
public class Param {
    /**
     * 是否缓存表达式
     */
    private boolean cached;

    /**
     * 脚本内容
     */
    private String content;

    /**
     * 上下文变量
     */
    private Map<String, Object> context;

    public Param() {
        this.cached = true;
        this.context = HashMap.newHashMap(NumberConstant.COMMON_SIZE);
    }

    public Param setContext(String name, Object value) {
        this.context.put(name, value);
        return this;
    }

    public boolean isCached() {
        return cached;
    }

    public Param setCached(boolean cached) {
        this.cached = cached;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Param setContent(String content) {
        this.content = content;
        return this;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public Param setContext(Map<String, Object> context) {
        this.context = context;
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Param.class.getSimpleName() + "[", "]")
                .add("cached=" + cached)
                .add("content='" + content + "'")
                .add("context=" + context)
                .toString();
    }
}

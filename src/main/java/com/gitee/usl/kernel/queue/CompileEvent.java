package com.gitee.usl.kernel.queue;

import cn.hutool.core.util.IdUtil;
import com.gitee.usl.kernel.configure.UslConfiguration;
import com.googlecode.aviator.Expression;

/**
 * 编译事件
 *
 * @author hongda.li
 */
public class CompileEvent {
    private final String eventId;
    private String key;
    private boolean cached;
    private String content;
    private Expression expression;
    private UslConfiguration configuration;

    public CompileEvent() {
        // 雪花ID作为唯一ID
        this.eventId = IdUtil.getSnowflakeNextIdStr();
    }

    public String getEventId() {
        return eventId;
    }

    public String getContent() {
        return content;
    }

    public String getKey() {
        return key;
    }

    public CompileEvent setKey(String key) {
        this.key = key;
        return this;
    }

    public CompileEvent setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isCached() {
        return cached;
    }

    public CompileEvent setCached(boolean cached) {
        this.cached = cached;
        return this;
    }

    public Expression getExpression() {
        return expression;
    }

    public CompileEvent setExpression(Expression expression) {
        this.expression = expression;
        return this;
    }

    public UslConfiguration getConfiguration() {
        return configuration;
    }

    public CompileEvent setConfiguration(UslConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }
}

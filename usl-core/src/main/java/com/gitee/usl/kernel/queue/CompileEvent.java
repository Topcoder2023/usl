package com.gitee.usl.kernel.queue;

import cn.hutool.core.util.IdUtil;
import com.gitee.usl.kernel.configure.Configuration;
import com.googlecode.aviator.Expression;

/**
 * 编译事件
 *
 * @author hongda.li
 */
public class CompileEvent {
    private final String eventId;
    private String content;
    private Expression expression;
    private Configuration configuration;

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

    public CompileEvent setContent(String content) {
        this.content = content;
        return this;
    }

    public Expression getExpression() {
        return expression;
    }

    public CompileEvent setExpression(Expression expression) {
        this.expression = expression;
        return this;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public CompileEvent setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }
}

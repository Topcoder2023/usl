package com.gitee.usl.infra.logger;

import org.slf4j.Marker;
import org.slf4j.event.Level;

/**
 * 格式化日志模板并根据日志级别向控制台输出彩色日志信息
 *
 * @author hongda.li
 */
public class UslLogger extends UslEnabledLogger {
    private final String name;

    public UslLogger(String name) {
        this.name = name;
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return this.name;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level, Marker marker, String s, Object[] objects, Throwable throwable) {
        System.out.println(s);
    }
}

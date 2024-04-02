package com.manda.usl.logger;

import org.slf4j.event.Level;

/**
 * @author hongda.li
 */
public interface LoggerProcessor {
    /**
     * 处理日志
     *
     * @param name      日志名称
     * @param level     日志级别
     * @param message  日志模板
     * @param throwable 异常信息
     * @param args      日志参数
     */
    void append(String name,
                Level level,
                String message,
                Throwable throwable,
                Object[] args);
}

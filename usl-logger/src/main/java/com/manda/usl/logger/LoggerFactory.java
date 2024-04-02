package com.manda.usl.logger;

import org.slf4j.ILoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定制化日志工厂
 *
 * @author hongda.li
 */
public final class LoggerFactory implements ILoggerFactory {
    /**
     * Logger 缓存
     * 避免反复多次创建重复的 Logger
     */
    private final Map<String, Logger> loggerCache = new ConcurrentHashMap<>(2 << 4);

    @Override
    public org.slf4j.Logger getLogger(String name) {
        return this.loggerCache.computeIfAbsent(name, Logger::new);
    }
}

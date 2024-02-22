package com.gitee.usl.logger;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * USL 定制化日志工厂
 *
 * @author hongda.li
 */
public final class USLLoggerFactory implements ILoggerFactory {
    /**
     * Logger 缓存
     * 避免反复多次创建重复的 Logger
     */
    private final Map<String, USLLogger> loggerCache;
    private final Map<String, Level> loggerLevelCache;

    public USLLoggerFactory() {
        this.loggerCache = new ConcurrentHashMap<>(2 << 4);
        this.loggerLevelCache = new ConcurrentHashMap<>(2 << 4);
    }

    @Override
    public Logger getLogger(String name) {
        return this.loggerCache.computeIfAbsent(name, this::newLogger);
    }

    private USLLogger newLogger(String name) {
        USLLogger logger = new USLLogger(name);
        this.loggerLevelCache.forEach((prefix, level) -> {
            if (name.startsWith(prefix)) {
                logger.setLevel(level);
            }
        });
        return logger;
    }

    public void resetLevel(Map<String, Level> levelMap) {
        this.loggerLevelCache.putAll(levelMap);
    }
}

package com.gitee.usl.infra.logger;

import com.gitee.usl.infra.constant.NumberConstant;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * USL 定制化日志工厂
 *
 * @author hongda.li
 */
public final class UslLoggerFactory implements ILoggerFactory {
    /**
     * Logger 缓存
     * 避免反复多次创建重复的 Logger
     */
    private final Map<String, UslLogger> loggerCache;

    public UslLoggerFactory() {
        loggerCache = new ConcurrentHashMap<>(NumberConstant.COMMON_SIZE);
    }

    @Override
    public Logger getLogger(String name) {
        return this.loggerCache.computeIfAbsent(name, UslLogger::new);
    }
}

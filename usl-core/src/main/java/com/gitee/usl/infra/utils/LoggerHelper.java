package com.gitee.usl.infra.utils;

import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.infra.constant.StringConstant;
import com.gitee.usl.infra.structure.StringMap;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author hongda.li
 */
public class LoggerHelper {
    private LoggerHelper() {
    }

    public static void resetLevel(StringMap<Level> loggerLevel) {
        ILoggerFactory factory = LoggerFactory.getILoggerFactory();
        Method method = ReflectUtil.getMethodByName(factory.getClass(), StringConstant.RESET_LEVEL_METHOD_NAME);
        Optional.ofNullable(method).ifPresent(m -> ReflectUtil.invoke(factory, m, loggerLevel));
    }

    public static void addLoggerFilter(Logger logger, Predicate<String> filter) {
        Method method = ReflectUtil.getMethodByName(logger.getClass(), StringConstant.ADD_LOGGER_FILTER_METHOD_NAME);
        Optional.ofNullable(method).ifPresent(m -> ReflectUtil.invoke(logger, m, filter));
    }

    public static void debug(Logger logger, String message, Supplier<Object[]> arguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(message, arguments.get());
        }
    }

    public static void info(Logger logger, String message, Supplier<Object[]> arguments) {
        if (logger.isInfoEnabled()) {
            logger.info(message, arguments.get());
        }
    }

    public static void warn(Logger logger, String message, Supplier<Object[]> arguments) {
        if (logger.isWarnEnabled()) {
            logger.warn(message, arguments.get());
        }
    }

    public static void error(Logger logger, String message, Supplier<Object[]> arguments) {
        if (logger.isErrorEnabled()) {
            logger.error(message, arguments.get());
        }
    }
}

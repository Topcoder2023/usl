package com.gitee.usl.infra.utils;

import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * @author hongda.li
 */
public class EnabledLogger {
    private EnabledLogger() {
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

package com.gitee.usl.function.base;

import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class LoggerFunction {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFunction.class);

    @Function({"logger_debug", "console_log"})
    public void debug(String message, Object... arguments) {
        LOGGER.debug(message, arguments);
    }

    @Function({"logger_warn", "console_warn"})
    public void warn(String message, Object... arguments) {
        LOGGER.warn(message, arguments);
    }

    @Function({"logger_info", "console_info"})
    public void info(String message, Object... arguments) {
        LOGGER.info(message, arguments);
    }

    @Function({"logger_error", "console_error"})
    public void error(String message, Object... arguments) {
        LOGGER.error(message, arguments);
    }
}

package com.gitee.usl.function.base;

import com.gitee.usl.api.annotation.Func;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
@Func
public class LoggerFunction {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFunction.class);

    @Func({"logger_debug", "console_log"})
    public void debug(String message, Object... arguments) {
        LOGGER.debug(message, arguments);
    }

    @Func({"logger_warn", "console_warn"})
    public void warn(String message, Object... arguments) {
        LOGGER.warn(message, arguments);
    }

    @Func({"logger_info", "console_info"})
    public void info(String message, Object... arguments) {
        LOGGER.info(message, arguments);
    }

    @Func({"logger_error", "console_error"})
    public void error(String message, Object... arguments) {
        LOGGER.error(message, arguments);
    }
}

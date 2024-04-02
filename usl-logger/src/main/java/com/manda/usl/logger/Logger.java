package com.manda.usl.logger;

import cn.hutool.core.util.ServiceLoaderUtil;
import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.helpers.LegacyAbstractLogger;

import java.io.Serial;
import java.util.List;

/**
 * 格式化日志模板并根据日志级别向控制台输出彩色日志信息
 *
 * @author hongda.li
 */
public class Logger extends LegacyAbstractLogger {
    @Serial
    private static final long serialVersionUID = -1L;

    private static volatile List<LoggerProcessor> processorList;

    public Logger(String name) {
        this.name = name;
    }

    @Override
    protected String getFullyQualifiedCallerName() {
        return this.name;
    }

    @Override
    protected void handleNormalizedLoggingCall(Level level,
                                               Marker marker,
                                               String format,
                                               Object[] arguments,
                                               Throwable throwable) {
        getProcessorList().forEach(processor -> {
            try {
                processor.append(this.getName(), level, format, throwable, arguments);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        });
    }

    public static List<LoggerProcessor> getProcessorList() {
        if (processorList != null) {
            return processorList;
        }
        synchronized (Logger.class) {
            if (processorList == null) {
                processorList = ServiceLoaderUtil.loadList(LoggerProcessor.class);
            }
        }
        return processorList;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

}

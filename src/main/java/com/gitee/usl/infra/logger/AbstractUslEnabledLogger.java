package com.gitee.usl.infra.logger;

import org.slf4j.helpers.LegacyAbstractLogger;

/**
 * 默认开启全部日志记录级别
 *
 * @author hongda.li
 */
public abstract class AbstractUslEnabledLogger extends LegacyAbstractLogger {
    @Override
    public boolean isTraceEnabled() {
        return true;
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

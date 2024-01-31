package com.gitee.usl.api.impl;

import com.gitee.usl.api.ExceptionHandler;
import com.gitee.usl.infra.exception.USLExecuteException;

/**
 * @author hongda.li
 */
public class DefaultExceptionHandler implements ExceptionHandler {
    @Override
    public void accept(Throwable throwable) {
        throw new USLExecuteException(throwable);
    }
}

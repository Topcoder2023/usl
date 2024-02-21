package com.gitee.usl.plugin.exception;

import com.gitee.usl.infra.exception.USLException;

/**
 * 校验异常
 *
 * @author hongda.li
 */
public class USLValidException extends USLException {

    public USLValidException(String message) {
        super(message);
    }

    public USLValidException(String message, Object... arguments) {
        super(message, arguments);
    }
}

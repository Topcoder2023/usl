package com.gitee.usl.infra.exception;

/**
 * USL 无法获取的异常
 *
 * @author hongda.li
 */
public class USLNotFoundException extends USLException {
    public USLNotFoundException() {
        super();
    }

    public USLNotFoundException(String message, Object... arguments) {
        super(message, arguments);
    }
}

package com.gitee.usl.infra.exception;

/**
 * USL 无法获取的异常
 *
 * @author hongda.li
 */
public class UslNotFoundException extends UslException {
    public UslNotFoundException() {
        super();
    }

    public UslNotFoundException(String message, Object... arguments) {
        super(message, arguments);
    }
}

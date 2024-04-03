package com.manda.usl.kernel.common;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author hongda.li
 */
public class USLException extends RuntimeException {
    private String message;
    private Object[] arguments;
    private Throwable throwable;

    public USLException() {
        super();
    }

    public USLException(String message) {
        super(message);
        this.message = message;
    }

    public USLException(String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments));
        this.message = message;
        this.arguments = arguments;
    }

    public USLException(Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
    }

    public USLException(String message, Throwable throwable) {
        super(message, throwable);
        this.message = message;
        this.throwable = throwable;
    }

    public USLException(String message, Throwable throwable, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments), throwable);
        this.message = message;
        this.arguments = arguments;
        this.throwable = throwable;
    }
}

package com.manda.usl.kernel.common;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
public class FuncException extends RuntimeException {

    private final Object[] arguments;

    private final Throwable throwable;

    public FuncException() {
        super();
        this.arguments = null;
        this.throwable = null;
    }

    public FuncException(String message) {
        super(message);
        this.arguments = null;
        this.throwable = null;
    }

    public FuncException(String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments));
        this.arguments = arguments;
        this.throwable = null;
    }

    public FuncException(Throwable throwable) {
        super(throwable);
        this.throwable = throwable;
        this.arguments = null;
    }

    public FuncException(String message, Throwable throwable) {
        super(message, throwable);
        this.throwable = throwable;
        this.arguments = null;
    }

    public FuncException(String message, Throwable throwable, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments), throwable);
        this.arguments = arguments;
        this.throwable = throwable;
    }
}

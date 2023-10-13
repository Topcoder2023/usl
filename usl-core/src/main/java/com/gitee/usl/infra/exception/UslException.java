package com.gitee.usl.infra.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * USL 通用异常
 * 支持在构造方法中传递异常信息模板与异常信息参数
 *
 * @author hongda.li
 */
public class UslException extends RuntimeException {
    public UslException() {
        super();
    }

    public UslException(String message) {
        super(message);
    }

    public UslException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public UslException(String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments));
    }

    public UslException(Throwable throwable) {
        super(throwable);
    }

    public UslException(Throwable throwable, String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments), throwable);
    }
}

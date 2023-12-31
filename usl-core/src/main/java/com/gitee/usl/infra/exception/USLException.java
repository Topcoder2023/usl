package com.gitee.usl.infra.exception;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.enums.ResultCode;
import lombok.Getter;
import lombok.Setter;

/**
 * USL 通用异常
 * 支持在构造方法中传递异常信息模板与异常信息参数
 *
 * @author hongda.li
 */
@Setter
@Getter
public class USLException extends RuntimeException {

    @Description("状态码")
    private ResultCode resultCode;

    public USLException() {
        super();
    }

    public USLException(String message) {
        super(message);
    }

    public USLException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public USLException(String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments));
    }

    public USLException(Throwable throwable) {
        super(throwable);
    }

    public USLException(Throwable throwable, String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments), throwable);
    }
}

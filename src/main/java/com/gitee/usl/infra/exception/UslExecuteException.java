package com.gitee.usl.infra.exception;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.infra.enums.ResultCode;

/**
 * @author hongda.li
 */
public class UslExecuteException extends UslException {
    private final ResultCode resultCode;

    public UslExecuteException() {
        super();
        this.resultCode = ResultCode.SUCCESS;
    }

    public UslExecuteException(ResultCode resultCode) {
        super();
        this.resultCode = resultCode;
    }

    public UslExecuteException(ResultCode resultCode, String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments));
        this.resultCode = resultCode;
    }

    public UslExecuteException(Throwable throwable) {
        super(throwable);
        this.resultCode = ResultCode.FAILURE;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}

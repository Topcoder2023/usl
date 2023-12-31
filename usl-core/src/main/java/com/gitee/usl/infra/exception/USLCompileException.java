package com.gitee.usl.infra.exception;

import com.gitee.usl.infra.enums.ResultCode;

/**
 * @author hongda.li
 */
public class USLCompileException extends USLException {

    public USLCompileException(Throwable throwable) {
        super(throwable);
        setResultCode(ResultCode.COMPILE_FAILURE);
    }

    public USLCompileException(ResultCode resultCode) {
        super();
        setResultCode(resultCode);
    }

    public USLCompileException(ResultCode resultCode, Object... arguments) {
        super(resultCode.name(), arguments);
        setResultCode(resultCode);
    }
}

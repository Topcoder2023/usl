package com.gitee.usl.infra.exception;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.infra.enums.ResultCode;
import lombok.Getter;

/**
 * @author hongda.li
 */
@Getter
public class USLExecuteException extends USLException {

    private static final long serialVersionUID = -2700399654507293658L;

    public USLExecuteException() {
        super();
        setResultCode(ResultCode.FAILURE);
    }

    public USLExecuteException(ResultCode resultCode) {
        super();
        setResultCode(resultCode);
    }

    public USLExecuteException(ResultCode resultCode, String message, Object... arguments) {
        super(CharSequenceUtil.format(message, arguments));
        setResultCode(resultCode);
    }

    public USLExecuteException(Throwable throwable) {
        super(throwable);
        setResultCode(ResultCode.FAILURE);
    }

}

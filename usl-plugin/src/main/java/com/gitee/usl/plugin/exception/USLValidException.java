package com.gitee.usl.plugin.exception;

import cn.hutool.core.text.CharSequenceUtil;
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
        super(CharSequenceUtil.format(message, arguments));
    }
}

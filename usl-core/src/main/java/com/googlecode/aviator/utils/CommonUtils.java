package com.googlecode.aviator.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;

/**
 * @author hongda.li
 */
public class CommonUtils {
    private CommonUtils() {
    }

    @Description("是否为非法的Java标识符")
    public static void checkJavaIdentifier(final String id) {
        if (id == null || id.isEmpty() || CharSequenceUtil.NULL.equals(id)) {
            throw new USLCompileException(ResultCode.ILLEGAL_NAME, CharSequenceUtil.EMPTY);
        }

        if (!Character.isJavaIdentifierStart(id.charAt(0))) {
            throw new USLCompileException(ResultCode.ILLEGAL_NAME, id.charAt(0));
        }

        for (char ch : id.substring(1).toCharArray()) {
            if (!Character.isJavaIdentifierPart(ch)) {
                throw new USLCompileException(ResultCode.ILLEGAL_NAME, ch);
            }
        }
    }
}

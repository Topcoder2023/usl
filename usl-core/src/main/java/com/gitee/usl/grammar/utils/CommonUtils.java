package com.gitee.usl.grammar.utils;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.infra.exception.USLCompileException;

/**
 * @author hongda.li
 */
public class CommonUtils {
    @Description("二元操作符")
    private static final char[] OPS = {'=', '>', '<', '+', '-', '*', '/', '%', '!', '&', '|'};

    @Description("十六进制字符")
    private static final char[] VALID_HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'a', 'B', 'b', 'C', 'c', 'D', 'd', 'E', 'e', 'F', 'f'};

    private CommonUtils() {
    }

    @Description("是否为有效的十六进制字符")
    public static boolean isValidHexChar(final char ch) {
        for (char c : VALID_HEX_CHAR) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    @Description("是否为二元操作符")
    public static boolean isBinaryOperator(final char ch) {
        for (char tmp : OPS) {
            if (tmp == ch) {
                return true;
            }
        }
        return false;
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

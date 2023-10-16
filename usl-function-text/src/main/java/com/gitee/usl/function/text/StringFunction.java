package com.gitee.usl.function.text;

import com.gitee.usl.api.annotation.Func;

import static cn.hutool.core.text.CharSequenceUtil.*;

/**
 * 字符串处理函数库
 *
 * @author hongda.li
 */
@Func
public class StringFunction {
    @Func("str.isBlank")
    public boolean strIsBlank(String str) {
        return isBlank(str);
    }

    @Func("str.isNotBlank")
    public boolean strIsNotBlank(String str) {
        return isNotBlank(str);
    }

    @Func("str.hasBlank")
    public boolean strHasBlank(String... strings) {
        return hasBlank(strings);
    }

    @Func("str.isAllBlank")
    public boolean strIsAllBlank(String... strings) {
        return isAllBlank(strings);
    }

    @Func("str.isEmpty")
    public boolean strIsEmpty(String str) {
        return isEmpty(str);
    }

    @Func("str.isNotEmpty")
    public boolean strIsNotEmpty(String str) {
        return isNotEmpty(str);
    }

    @Func("str.emptyIfNull")
    public String strEmptyIfNull(String str) {
        return emptyIfNull(str);
    }

    @Func("str.nullToEmpty")
    public String strNullToEmpty(String str) {
        return nullToEmpty(str);
    }

    @Func("str.nullToDefault")
    public String strNullToDefault(String str, String defaultStr) {
        return nullToDefault(str, defaultStr);
    }

    @Func("str.emptyToDefault")
    public String strEmptyToDefault(String str, String defaultStr) {
        return emptyToDefault(str, defaultStr);
    }
}

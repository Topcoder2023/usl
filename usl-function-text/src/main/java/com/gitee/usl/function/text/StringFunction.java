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

    @Func("str.blankToDefault")
    public String strBlankToDefault(String str, String defaultStr) {
        return blankToDefault(str, defaultStr);
    }

    @Func("str.emptyToNull")
    public String strEmptyToNull(String str) {
        return emptyToNull(str);
    }

    @Func("str.hasEmpty")
    public boolean strHasEmpty(String... strings) {
        return hasEmpty(strings);
    }

    @Func("str.isAllEmpty")
    public boolean strIsAllEmpty(String... strings) {
        return isAllEmpty(strings);
    }

    @Func("str.isAllNotEmpty")
    public boolean strIsAllNotEmpty(String... strings) {
        return isAllNotEmpty(strings);
    }

    @Func("str.isAllNotBlank")
    public boolean strIsAllNotBlank(String... strings) {
        return isAllNotBlank(strings);
    }
}

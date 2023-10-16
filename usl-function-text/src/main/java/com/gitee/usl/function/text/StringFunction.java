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

    @Func("str.isEmpty")
    public boolean strIsEmpty(String str) {
        return isEmpty(str);
    }

    @Func("str.isBlank")
    public boolean strIsBlank(String str) {
        return isBlank(str);
    }

    @Func("str.emptyToDefault")
    public String strEmptyToDefault(String str, String defaultStr) {
        return emptyToDefault(str, defaultStr);
    }
}

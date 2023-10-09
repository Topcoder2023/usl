package com.gitee.usl.app.function.text;

import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.constant.NumberConstant;

/**
 * @author hongda.li
 */
@Func
public class StringFunction {

    @Func({"str.size", "str.length"})
    public int size(String str) {
        return str == null ? NumberConstant.ZERO : str.length();
    }
}

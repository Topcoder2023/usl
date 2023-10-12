package com.gitee.usl.app.function.text;

import com.gitee.usl.api.annotation.Func;

/**
 * @author hongda.li
 */
@Func
public class StringFunction {

    @Func({"str.size", "str.length"})
    public int size(String str) {
        throw new NullPointerException();
    }
}

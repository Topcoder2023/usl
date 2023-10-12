package com.gitee.usl.app.function.text;

import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.api.annotation.Retryable;
import com.gitee.usl.infra.constant.NumberConstant;

/**
 * @author hongda.li
 */
@Func
public class StringFunction {

    @Func({"str.size", "str.length"})
    @Retryable
    public int size(String str) {
        throw new NullPointerException();
    }
}

package com.gitee.usl.function.base;

import cn.hutool.core.convert.Convert;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Func;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hongda.li
 */
@Func
public class ConvertFunction {

    @Func("to.long")
    public Long toLong(Object value) {
        return Convert.toLong(value);
    }

    @Func("to.double")
    public Double toDouble(Object value) {
        return Convert.toDouble(value);
    }

    @Func("to.bigDecimal")
    public BigDecimal toBigDecimal(Object value) {
        return Convert.toBigDecimal(value);
    }

    @Func("to.boolean")
    public Boolean toBoolean(Object value) {
        return Convert.toBool(value);
    }

    @Func("to.string")
    public String toStr(Object value) {
        return Convert.toStr(value);
    }

    @Func("to.date")
    public Date toDate(Object value) {
        return Convert.toDate(value);
    }

    @Func("to.json")
    public String toJson(Object obj) {
        return JsonKit.toJson(obj);
    }
}

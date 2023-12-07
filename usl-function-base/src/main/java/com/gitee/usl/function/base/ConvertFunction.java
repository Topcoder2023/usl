package com.gitee.usl.function.base;

import cn.hutool.core.convert.Convert;
import cn.zhxu.xjson.JsonKit;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ConvertFunction {

    @Function("to_long")
    public Long toLong(Object value) {
        return Convert.toLong(value);
    }

    @Function("to_double")
    public Double toDouble(Object value) {
        return Convert.toDouble(value);
    }

    @Function("to_bigDecimal")
    public BigDecimal toBigDecimal(Object value) {
        return Convert.toBigDecimal(value);
    }

    @Function("to_boolean")
    public Boolean toBoolean(Object value) {
        return Convert.toBool(value);
    }

    @Function("to_string")
    public String toStr(Object value) {
        return Convert.toStr(value);
    }

    @Function("to_date")
    public Date toDate(Object value) {
        return Convert.toDate(value);
    }

    @Function("to_json")
    public String toJson(Object obj) {
        return JsonKit.toJson(obj);
    }
}

package com.gitee.usl.function.base;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

/**
 * @author hongda.li
 */
@FunctionGroup
public class DateFunction {

    @Function("date")
    public DateTime date() {
        return DateUtil.date();
    }

    @Function("date")
    public DateTime date(long timestamp) {
        return DateUtil.date(timestamp);
    }
}

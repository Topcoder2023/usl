package com.gitee.usl.infra.constant;

import cn.hutool.core.date.format.FastDateFormat;
import com.gitee.usl.api.annotation.Description;

/**
 * @author hongda.li
 */
public class AsmConstants {
    private AsmConstants() {
    }


    @Description("函数参数定义")
    public static final String FUNC_PARAMS_VAR = "__functions_args__";

    @Description("日期时间格式化")
    public static final FastDateFormat DATETIME_FORMAT = FastDateFormat.getInstance("yyyy_MM_dd_HH_mm_ss");

}

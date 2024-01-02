package com.gitee.usl.infra.constant;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.asm.CS;

/**
 * @author hongda.li
 */
public class AsmConstants {
    private AsmConstants() {
    }

    @Description("函数参数定义")
    public static final String FUNC_PARAMS_VAR = "__functions_args__";

    @Description("类名前缀")
    public static final String CLASS_NAME_PREFIX = "Script_";

    @Description("日期时间格式化")
    public static final FastDateFormat DATETIME_FORMAT;

    @Description("类全路径")
    public static final String EXPRESSION_CLASS_NAME;

    static {
        DATETIME_FORMAT = FastDateFormat.getInstance("yyyy_MM_dd_HH_mm_ss");

        EXPRESSION_CLASS_NAME = ClassUtil.getPackagePath(CS.class) + StrPool.SLASH + CS.class.getSimpleName();
    }
}

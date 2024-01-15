package com.gitee.usl.infra.constant;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.asm.CS;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.gitee.usl.grammar.type.USLFunction;
import com.gitee.usl.grammar.type.USLBigInt;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.type.USLObject;

import java.util.List;

/**
 * @author hongda.li
 */
public class AsmConstants {
    private AsmConstants() {
    }

    public static final String CLASS_DESC_LEFT = "(";

    public static final String CLASS_DESC_RIGHT = ")";

    public static final String CLASS_DESC_PREFIX = "L";

    public static final String CLASS_DESC_SUFFIX = "V";

    public static final String CLASS_DESC_SPLIT = ";";

    public static final String RUNTIME_PATH = buildClassPath(RuntimeUtils.class);

    public static final String SYMBOL_TABLE_DESC = CLASS_DESC_PREFIX + buildClassPath(ScriptKeyword.class);

    public static final String USL_FUNCTION_TYPE_PATH = buildClassPath(USLFunction.class);

    public static final String USL_FUNCTION_TYPE_DESC = CLASS_DESC_PREFIX + USL_FUNCTION_TYPE_PATH;

    public static final String USL_JAVA_TYPE_PATH = buildClassPath(AviatorJavaType.class);

    public static final String USL_JAVA_TYPE_DESC = CLASS_DESC_PREFIX + USL_JAVA_TYPE_PATH;

    public static final String USL_OBJECT_TYPE_PATH = buildClassPath(USLObject.class);

    public static final String USL_OBJECT_TYPE_DESC = CLASS_DESC_PREFIX + USL_OBJECT_TYPE_PATH + CLASS_DESC_SPLIT;

    public static final String USL_BIG_INT_TYPE_PATH = buildClassPath(USLBigInt.class);

    public static final String USL_BIG_INT_TYPE_DESC = CLASS_DESC_PREFIX + USL_BIG_INT_TYPE_PATH + CLASS_DESC_SPLIT;

    @Description("脚本引擎类路径")
    public static final String SCRIPT_ENGINE_PATH = buildClassPath(ScriptEngine.class);

    @Description("脚本引擎的类描述")
    public static final String SCRIPT_ENGINE_DESC = CLASS_DESC_PREFIX + SCRIPT_ENGINE_PATH;

    @Description("List类型的类描述")
    public static final String LIST_DESC = CLASS_DESC_PREFIX + buildClassPath(List.class);

    @Description("String类型的类描述")
    public static final String STRING_DESC = CLASS_DESC_PREFIX + buildClassPath(String.class);

    @Description("函数参数定义")
    public static final String FUNC_PARAMS_VAR = "__functions_args__";

    @Description("类名前缀")
    public static final String CLASS_NAME_PREFIX = "Script_";

    @Description("日期时间格式化")
    public static final FastDateFormat DATETIME_FORMAT = FastDateFormat.getInstance("yyyy_MM_dd_HH_mm_ss");

    @Description("类全路径")
    public static final String EXPRESSION_CLASS_NAME = buildClassPath(CS.class);

    private static String buildClassPath(Class<?> clz) {
        return ClassUtil.getPackagePath(clz) + StrPool.SLASH + clz.getSimpleName();
    }

}

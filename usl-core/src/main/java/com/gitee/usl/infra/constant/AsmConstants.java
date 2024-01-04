package com.gitee.usl.infra.constant;

import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.ClassUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.ScriptKeyword;
import com.gitee.usl.grammar.asm.CS;

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

    public static final String SYMBOL_TABLE_DESC = CLASS_DESC_PREFIX + buildClassPath(ScriptKeyword.class);

    @Description("脚本引擎类路径")
    public static final String SCRIPT_ENGINE_PATH = "com/gitee/usl/grammar/ScriptEngine";

    @Description("脚本引擎的类描述")
    public static final String SCRIPT_ENGINE_DESC = CLASS_DESC_PREFIX + SCRIPT_ENGINE_PATH;

    @Description("List类型的类描述")
    public static final String LIST_DESC = CLASS_DESC_PREFIX + "java/util/List";

    @Description("String类型的类描述")
    public static final String STRING_DESC = CLASS_DESC_PREFIX + "java/lang/String";

    @Description("函数参数定义")
    public static final String FUNC_PARAMS_VAR = "__functions_args__";

    @Description("函数参数索引")
    public static final String FUNC_PARAMS_INDEX = "__functions_index__";

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

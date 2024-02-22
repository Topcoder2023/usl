package com.gitee.usl.function.base;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;


/**
 * @author jingshu.zeng
 */


@FunctionGroup
public class AmountFunction {
    @Function("amount_to_capital_chi")
    public static String amount_to_capital_chi(double number) {
        return Convert.digitToChinese(number);
    }

    @Function("amount_to_capital_eng")
    public static String amount_to_capital_eng(double number) {
        return Convert.numberToWord(100.23);
    }
}




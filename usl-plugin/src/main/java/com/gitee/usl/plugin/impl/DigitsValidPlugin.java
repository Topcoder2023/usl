package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Digits;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * 数字格式验证插件
 * 将数字格式注解校验逻辑应用到函数执行中
 * @author jingshu.zeng
 */
public class DigitsValidPlugin extends AbstractValidPlugin<Digits> {

    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Digits annotation, Object actual) {
        // 获取Digits注解中定义的整数位数和小数位数
        int integer = annotation.integer();
        int fraction = annotation.fraction();

        // 参数实际值校验
        if (actual instanceof Number) {
            // 如果是数字类型，判断其整数位数和小数位数是否符合要求
            String valueStr = String.valueOf(actual);
            int dotIndex = valueStr.indexOf('.');
            int intPartLength = dotIndex >= 0 ? dotIndex : valueStr.length();
            int fractionPartLength = dotIndex >= 0 ? valueStr.length() - dotIndex - 1 : 0;

            if (intPartLength > integer || fractionPartLength > fraction) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", String.valueOf(actual))
                        .replace("{integer}", String.valueOf(integer))
                        .replace("{fraction}", String.valueOf(fraction));

                // 抛出校验异常
                throw new USLValidException(replace, location, actual, integer + "." + fraction);
            }
        } else if (actual instanceof String) {
            // 如果是字符串类型，先尝试将其转换为数字类型，然后再进行校验
            try {
                double value = Double.parseDouble((String) actual);
                valid(location, annotation, value);
            } catch (NumberFormatException e) {
                // 如果无法转换为数字类型，则直接抛出校验异常
                throw new USLValidException("The parameter must be a valid number", location, actual, integer + "." + fraction);
            }
        } else {
            // 如果既不是数字类型也不是字符串类型，则抛出异常
            throw new USLValidException("The parameter must be either a Number or a String", location, actual, integer + "." + fraction);
        }
    }
}

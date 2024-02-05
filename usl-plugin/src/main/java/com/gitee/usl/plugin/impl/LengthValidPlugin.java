package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Length;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * 长度校验插件
 * 根据Length注解对函数参数进行长度校验
 *
 * @author jingshu.zeng
 */
public class LengthValidPlugin extends AbstractValidPlugin<Length> {


    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Length annotation, Object actual) {
        // Length注解预期的长度
        int exactLength = annotation.exactLength();
        int minLength = annotation.minLength();
        int maxLength = annotation.maxLength();

        // 参数实际值校验
        if (actual instanceof CharSequence) {
            int actualLength = ((CharSequence) actual).length();

            // 校验长度范围
            if ((minLength != -1 && actualLength < minLength) || (maxLength != -1 && actualLength > maxLength) || (exactLength != -1 && actualLength != exactLength)) {

                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", String.valueOf(actual))
                        .replace("{exactLength}", String.valueOf(exactLength))
                        .replace("{minLength}", String.valueOf(minLength))
                        .replace("{maxLength}", String.valueOf(maxLength));

                // 抛出校验异常
                throw new USLValidException(replace);
            }
        }
    }
}

package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.Length;
import com.manda.usl.plugin.entity.Location;

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
        if (actual instanceof CharSequence cs) {
            int actualLength = cs.length();

            // 校验长度范围
            boolean less = minLength != -1 && actualLength < minLength;
            boolean more = maxLength != -1 && actualLength > maxLength;
            boolean none = exactLength != -1 && actualLength != exactLength;
            if (less || more || none) {

                // 注解指定的错误信息
                String message = annotation.message();

                int expect;
                if (less) {
                    expect = minLength;
                } else if (more) {
                    expect = maxLength;
                } else {
                    expect = exactLength;
                }

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", String.valueOf(actualLength))
                        .replace("{expect}", String.valueOf(expect));

                // 抛出校验异常
                throw new FuncException(replace,
                        location.getName(),
                        location.getIndex(),
                        actualLength,
                        less,
                        more,
                        none);
            }
        }
    }
}

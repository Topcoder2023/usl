package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.USLException;
import com.manda.usl.kernel.entity.FunctionSession;
import com.manda.usl.plugin.annotation.DecimalMin;
import com.manda.usl.plugin.entity.Location;

/**
 * 最小值校验插件
 *
 * @author jingshu.zeng
 */
public class DecimalMinValidPlugin extends AbstractValidPlugin<DecimalMin> {

    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, DecimalMin annotation, Object actual) {
        // DecimalMin 注解预期的最大值
        String accept = annotation.value();

        // 参数实际值校验
        if (actual instanceof Number && Double.parseDouble(actual.toString()) < Long.parseLong(accept)) {
            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual))
                    .replace("{max}", accept);

            // 抛出校验异常
            throw new USLException(replace, location, actual, accept);
        }
    }
}

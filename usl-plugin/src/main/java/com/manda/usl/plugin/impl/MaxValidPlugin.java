package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.Max;
import com.manda.usl.plugin.view.Location;

/**
 * @author hongda.li
 */
public class MaxValidPlugin extends AbstractValidPlugin<Max> {
    @Override
    public void onBegin(FunctionSession session) {
        this.matchAnnotation(session);
    }

    @Override
    protected void valid(Location location, Max annotation, Object actual) {
        // Max注解预期的最大值
        long accept = annotation.value();

        // 参数实际值校验
        if (actual instanceof Number && ((Number) actual).longValue() > accept) {

            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual))
                    .replace("{max}", String.valueOf(accept));

            // 抛出校验异常
            throw new FuncException(replace, location, actual, accept);
        }
    }
}

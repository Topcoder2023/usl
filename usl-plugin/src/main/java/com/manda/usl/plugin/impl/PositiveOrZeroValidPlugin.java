package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.PositiveOrZero;
import com.manda.usl.plugin.view.Location;

/**
 * @author jiahao.song
 */
public class PositiveOrZeroValidPlugin extends AbstractValidPlugin<PositiveOrZero> {
    @Override
    public void onBegin(FunctionSession session) {
        this.matchAnnotation(session);
    }

    @Override
    protected void valid(Location location, PositiveOrZero annotation, Object actual) {

        // 参数实际值校验
        if (actual instanceof Number && ((Number) actual).longValue() < 0) {

            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual));

            // 抛出校验异常
            throw new FuncException(replace, location, actual);
        }
    }
}

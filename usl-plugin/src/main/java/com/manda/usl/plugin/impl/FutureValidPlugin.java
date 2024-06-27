package com.manda.usl.plugin.impl;


import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.Future;
import com.manda.usl.plugin.entity.Location;

import java.util.Calendar;
import java.util.Date;

/**
 * @author jingshu.zeng
 */
public class FutureValidPlugin extends AbstractValidPlugin<Future> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Future annotation, Object actual) {
        // 获取当前时间
        Date now = new Date();

        // 参数实际值校验
        if (actual instanceof Date actualDate) {
            if (actualDate.before(now)) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", actualDate.toString())
                        .replace("{now}", now.toString());

                // 抛出校验异常
                throw new FuncException(replace, location, actual, now);
            }
        } else if (actual instanceof Calendar actualCalendar) {
            if (actualCalendar.before(Calendar.getInstance())) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", actualCalendar.getTime().toString())
                        .replace("{now}", Calendar.getInstance().getTime().toString());

                // 抛出校验异常
                throw new FuncException(replace, location, actual, Calendar.getInstance());
            }
        } else {
            throw new IllegalArgumentException("Invalid argument type. Expected: Date or Calendar");
        }
    }
}

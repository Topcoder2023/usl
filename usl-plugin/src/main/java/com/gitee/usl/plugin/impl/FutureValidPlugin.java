package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Future;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

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
        if (actual instanceof Date) {
            Date actualDate = (Date) actual;
            if (actualDate.before(now)) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", actualDate.toString())
                        .replace("{now}", now.toString());

                // 抛出校验异常
                throw new USLValidException(replace, location, actual, now);
            }
        } else if (actual instanceof Calendar) {
            Calendar actualCalendar = (Calendar) actual;
            if (actualCalendar.before(Calendar.getInstance())) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", actualCalendar.getTime().toString()) // 修改此处以显示实际日期
                        .replace("{now}", Calendar.getInstance().getTime().toString()); // 修改此处以显示当前日期

                // 抛出校验异常
                throw new USLValidException(replace, location, actual, Calendar.getInstance());
            }
        } else {
            throw new IllegalArgumentException("Invalid argument type. Expected: Date or Calendar");
        }
    }
}

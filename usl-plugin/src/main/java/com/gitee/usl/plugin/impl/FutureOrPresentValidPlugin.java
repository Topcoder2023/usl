package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.FutureOrPresent;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

import java.util.Calendar;
import java.util.Date;
/**
 * @author jingshu.zeng
 */

public class FutureOrPresentValidPlugin extends AbstractValidPlugin<FutureOrPresent> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, FutureOrPresent annotation, Object actual) {
        // 获取当前日期
        Calendar nowCalendar = Calendar.getInstance();
        nowCalendar.add(Calendar.SECOND, -1);
        long nowTimestamp = nowCalendar.getTimeInMillis();

        // 参数实际值校验
        if (actual instanceof Date) {
            Date actualDate = (Date) actual;
            long actualTimestamp = actualDate.getTime();
            if (actualTimestamp < nowTimestamp) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", actualDate.toString())
                        .replace("{now}", nowCalendar.getTime().toString());

                // 抛出校验异常
                throw new USLValidException(replace, location, actual, nowCalendar.getTime());
            }
        } else if (actual instanceof Calendar) {
            Calendar actualCalendar = (Calendar) actual;
            long actualTimestamp = actualCalendar.getTimeInMillis();
            if (actualTimestamp < nowTimestamp) {
                // 注解指定的错误信息
                String message = annotation.message();

                // 替换预置变量
                String replace = message.replace("{name}", location.getName())
                        .replace("{index}", String.valueOf(location.getIndex()))
                        .replace("{value}", actualCalendar.getTime().toString()) // 修改此处以显示实际日期
                        .replace("{now}", nowCalendar.getTime().toString()); // 修改此处以显示当前日期

                // 抛出校验异常
                throw new USLValidException(replace, location, actual, nowCalendar.getTime());
            }
        } else {
            throw new IllegalArgumentException("Invalid argument type. Expected: Date or Calendar");
        }
    }

}

package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.PastOrPresent;
import com.manda.usl.plugin.entity.Location;

import java.time.LocalDateTime;

/**
 * @author jiahao.song
 */
public class PastOrPresentValidPlugin extends AbstractValidPlugin<PastOrPresent> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, PastOrPresent annotation, Object actual) {

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 参数实际值校验
        if (actual instanceof LocalDateTime actualTime) {
            // 校验时间是否在过去（包括当前）
            if (actualTime.isAfter(now)) {
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
}

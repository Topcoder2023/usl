package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.NotNull;
import com.manda.usl.plugin.entity.Location;

/**
 * @author jiahao.song, jingshu.zeng
 */
public class NotNullValidPlugin extends AbstractValidPlugin<NotNull> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, NotNull annotation, Object actual) {

        // 参数实际值校验
        if (actual == null) {

            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()));

            // 抛出校验异常
            throw new FuncException(replace, location);
        }
    }
}

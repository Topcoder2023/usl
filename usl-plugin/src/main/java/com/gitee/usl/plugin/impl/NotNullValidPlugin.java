package com.gitee.usl.plugin.impl;

import cn.hutool.core.util.ObjectUtil;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.NotNull;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * @author hongda.li
 */
public class NotNullValidPlugin extends AbstractValidPlugin<NotNull> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, NotNull annotation, Object actual) {
        if (ObjectUtil.isEmpty(actual)) {
            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()));

            // 抛出校验异常
            throw new USLValidException(replace, location);
        }
    }
}

package com.manda.usl.plugin.impl;

import cn.hutool.core.util.ObjectUtil;
import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.NotEmpty;
import com.manda.usl.plugin.entity.Location;

/**
 * @author hongda.li
 */
public class NotEmptyValidPlugin extends AbstractValidPlugin<NotEmpty> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, NotEmpty annotation, Object actual) {
        if (ObjectUtil.isEmpty(actual)) {
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

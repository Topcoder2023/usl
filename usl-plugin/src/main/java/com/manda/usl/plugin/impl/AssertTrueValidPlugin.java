package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.common.FuncException;
import com.manda.usl.kernel.view.FunctionSession;
import com.manda.usl.plugin.annotation.AssertTrue;
import com.manda.usl.plugin.view.Location;

/**
 * 真值校验插件
 *
 * @author jingshu.zeng
 */
public class AssertTrueValidPlugin extends AbstractValidPlugin<AssertTrue> {

    @Override
    public void onBegin(FunctionSession session) {
        this.matchAnnotation(session);
    }

    @Override
    protected void valid(Location location, AssertTrue annotation, Object actual) {
        // 参数实际值校验
        if (!Boolean.TRUE.equals(actual)) {
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

package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.AssertFalse;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * 假值校验插件
 * @author jingshu.zeng
 */
public class AssertFalseValidPlugin extends AbstractValidPlugin<AssertFalse> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, AssertFalse annotation, Object actual) {
        // 参数实际值校验
        if (actual instanceof Boolean && ((Boolean) actual)) {
            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual));

            // 抛出校验异常
            throw new USLValidException(replace, location, actual);
        }
    }
}

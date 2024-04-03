package com.manda.usl.plugin.impl;

import com.manda.usl.kernel.annotation.Singleton;
import com.manda.usl.kernel.common.USLException;
import com.manda.usl.kernel.entity.FunctionSession;
import com.manda.usl.plugin.annotation.AssertFalse;
import com.manda.usl.plugin.entity.Location;

/**
 * 假值校验插件
 *
 * @author jingshu.zeng
 */
@Singleton(whenNotExist = AssertFalseValidPlugin.class)
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
                    .replace("{value}", String.valueOf(true));

            // 抛出校验异常
            throw new USLException(replace, location, true);
        }
    }
}

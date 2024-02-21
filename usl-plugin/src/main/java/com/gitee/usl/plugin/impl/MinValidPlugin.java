package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Min;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * @author jingshu.zeng
 */
public class MinValidPlugin extends AbstractValidPlugin<Min> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Min annotation, Object actual) {
        // Min注解预期的最小值
        long accept = annotation.value();

        // 参数实际值校验
        if (actual instanceof Number && ((Number) actual).longValue() < accept) {

            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual))
                    .replace("{min}", String.valueOf(accept));

            // 抛出校验异常
            throw new USLValidException(replace, location, actual, accept);
        }
    }
}

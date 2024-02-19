package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.DecimalMax;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * 最大值校验插件
 * @author jingshu.zeng
 */
public class DecimalMaxValidPlugin extends AbstractValidPlugin<DecimalMax> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, DecimalMax annotation, Object actual) {
        // Max注解预期的最大值
        String accept = annotation.value();

        // 参数实际值校验
        if (actual instanceof Number && ((Number) actual).longValue() > Long.parseLong(accept)) {

            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual))
                    .replace("{max}", accept);

            // 抛出校验异常
            throw new USLValidException(replace, location, actual, accept);
        }
    }
}

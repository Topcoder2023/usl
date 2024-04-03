package com.manda.usl.plugin.impl;

import cn.hutool.core.util.ReUtil;
import com.manda.usl.kernel.common.USLException;
import com.manda.usl.kernel.entity.FunctionSession;
import com.manda.usl.plugin.annotation.Pattern;
import com.manda.usl.plugin.entity.Location;

/**
 * @author jiahao.song
 */
public class PatternValidPlugin extends AbstractValidPlugin<Pattern> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Pattern annotation, Object actual) {
        // 获取正则表达式
        String regex = annotation.regexp();

        // 参数实际值校验
        if (actual instanceof CharSequence cs && !ReUtil.isMatch(regex, cs)) {
            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual))
                    .replace("{regex}", regex);

            // 抛出校验异常
            throw new USLException(replace, location, actual, regex);
        }
    }
}

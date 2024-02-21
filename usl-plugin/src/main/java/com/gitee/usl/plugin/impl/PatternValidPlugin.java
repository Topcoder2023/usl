package com.gitee.usl.plugin.impl;

import cn.hutool.core.util.ReUtil;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Pattern;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

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
            throw new USLValidException(replace, location, actual, regex);
        }
    }
}

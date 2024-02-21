package com.gitee.usl.plugin.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.NotBlank;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;

/**
 * @author jiahao.song
 */
public class NotBlankValidPlugin extends AbstractValidPlugin<NotBlank> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, NotBlank annotation, Object actual) {

        // 参数实际值校验
        if (actual instanceof CharSequence cs && CharSequenceUtil.isBlank(cs)) {

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

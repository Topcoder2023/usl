package com.manda.usl.plugin.impl;

import cn.hutool.core.lang.Validator;
import com.manda.usl.kernel.common.USLException;
import com.manda.usl.kernel.entity.FunctionSession;
import com.manda.usl.plugin.annotation.Email;
import com.manda.usl.plugin.entity.Location;

/**
 * @author jingshu.zeng
 */
public class EmailValidPlugin extends AbstractValidPlugin<Email> {
    @Override
    public void onBegin(FunctionSession session) {
        this.filterAnnotation(session);
    }

    @Override
    protected void valid(Location location, Email annotation, Object actual) {
        // 参数实际值校验
        if (actual instanceof String string && !Validator.isEmail(string)) {

            // 注解指定的错误信息
            String message = annotation.message();

            // 替换预置变量
            String replace = message.replace("{name}", location.getName())
                    .replace("{index}", String.valueOf(location.getIndex()))
                    .replace("{value}", String.valueOf(actual));

            // 抛出校验异常
            throw new USLException(replace, location, actual);
        }
    }
}

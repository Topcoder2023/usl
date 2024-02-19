package com.gitee.usl.plugin.impl;

import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.plugin.annotation.Email;
import com.gitee.usl.plugin.domain.Location;
import com.gitee.usl.plugin.exception.USLValidException;
import java.util.regex.Pattern;

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
        if (actual instanceof String && ! isValidEmail((String) actual)) {

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
    /**
     * 判断字符串是否符合邮箱格式
     *
     * @param email 邮箱字符串
     * @return true表示符合邮箱格式，false表示不符合邮箱格式
     */


    private boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";
        return Pattern.matches(regex, email);
    }

}

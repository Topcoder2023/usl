package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Email;
import com.gitee.usl.plugin.impl.EmailValidPlugin;

/**
 * Email注解函数增强器
 * 将Email注解校验器插件安装到指定函数中
 *
 * @author jingshu.zeng
 */
public class EmailValidEnhancer extends AbstractValidEnhancer<Email> {

    @Override
    protected Plugin provide() {
        return Singleton.get(EmailValidPlugin.class);
    }
}

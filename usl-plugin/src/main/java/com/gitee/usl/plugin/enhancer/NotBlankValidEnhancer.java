package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.NotBlank;
import com.gitee.usl.plugin.impl.NotBlankValidPlugin;

/**
 * 不为空白注解函数增强器
 * 将不为空白注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class NotBlankValidEnhancer extends AbstractValidEnhancer<NotBlank> {

    @Override
    protected Plugin provide() {
        return Singleton.get(NotBlankValidPlugin.class);
    }
}

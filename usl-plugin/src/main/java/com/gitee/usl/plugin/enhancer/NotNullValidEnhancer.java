package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.NotNull;
import com.gitee.usl.plugin.impl.NotNullValidPlugin;

/**
 * 不为空注解函数增强器
 * 将不为空注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class NotNullValidEnhancer extends AbstractValidEnhancer<NotNull> {

    @Override
    protected Plugin provide() {
        return Singleton.get(NotNullValidPlugin.class);
    }
}

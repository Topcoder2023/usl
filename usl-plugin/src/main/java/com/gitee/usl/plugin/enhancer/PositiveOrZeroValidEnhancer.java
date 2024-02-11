package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.PositiveOrZero;
import com.gitee.usl.plugin.impl.PositiveOrZeroValidPlugin;

/**
 * 非负校验注解函数增强器
 * 将非负校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class PositiveOrZeroValidEnhancer extends AbstractValidEnhancer<PositiveOrZero> {

    @Override
    protected Plugin provide() {
        return Singleton.get(PositiveOrZeroValidPlugin.class);
    }
}

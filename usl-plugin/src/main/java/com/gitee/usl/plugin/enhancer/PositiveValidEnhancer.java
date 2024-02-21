package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Positive;
import com.gitee.usl.plugin.impl.PositiveValidPlugin;

/**
 * 正数校验注解函数增强器
 * 将正数校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class PositiveValidEnhancer extends AbstractValidEnhancer<Positive> {

    @Override
    protected Plugin provide() {
        return Singleton.get(PositiveValidPlugin.class);
    }
}

package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Null;
import com.gitee.usl.plugin.impl.NullValidPlugin;

/**
 * 空注解函数增强器
 * 将空注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class NullValidEnhancer extends AbstractValidEnhancer<Null> {

    @Override
    protected Plugin provide() {
        return Singleton.get(NullValidPlugin.class);
    }
}

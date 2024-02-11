package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Past;
import com.gitee.usl.plugin.impl.PastValidPlugin;

/**
 * 时间校验注解函数增强器
 * 将时间校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class PastValidEnhancer extends AbstractValidEnhancer<Past> {

    @Override
    protected Plugin provide() {
        return Singleton.get(PastValidPlugin.class);
    }
}

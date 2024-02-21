package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Max;
import com.gitee.usl.plugin.impl.MaxValidPlugin;

/**
 * 最大值注解函数增强器
 * 将最大值注解校验器插件安装到指定函数中
 *
 * @author hongda.li
 */
public class MaxValidEnhancer extends AbstractValidEnhancer<Max> {

    @Override
    protected Plugin provide() {
        return Singleton.get(MaxValidPlugin.class);
    }
}

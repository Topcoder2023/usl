package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.NegativeOrZero;
import com.gitee.usl.plugin.impl.NegativeOrZeroValidPlugin;

/**
 * 非正校验注解函数增强器
 * 将非正校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class NegativeOrZeroValidEnhancer extends AbstractValidEnhancer<NegativeOrZero> {

    @Override
    protected Plugin provide() {
        return Singleton.get(NegativeOrZeroValidPlugin.class);
    }
}

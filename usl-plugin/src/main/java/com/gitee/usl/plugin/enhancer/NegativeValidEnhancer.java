package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Negative;
import com.gitee.usl.plugin.impl.NegativeValidPlugin;

/**
 * 负数校验注解函数增强器
 * 将负数校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class NegativeValidEnhancer extends AbstractValidEnhancer<Negative> {

    @Override
    protected Plugin provide() {
        return Singleton.get(NegativeValidPlugin.class);
    }
}

package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Pattern;
import com.gitee.usl.plugin.impl.PatternValidPlugin;

/**
 * 正则校验注解函数增强器
 * 将正则校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class PatternValidEnhancer extends AbstractValidEnhancer<Pattern> {

    @Override
    protected Plugin provide() {
        return Singleton.get(PatternValidPlugin.class);
    }
}

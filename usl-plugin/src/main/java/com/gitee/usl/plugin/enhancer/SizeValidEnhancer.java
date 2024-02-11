package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Size;
import com.gitee.usl.plugin.impl.SizeValidPlugin;

/**
 * 长度校验注解函数增强器
 * 将长度校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class SizeValidEnhancer extends AbstractValidEnhancer<Size> {

    @Override
    protected Plugin provide() {
        return Singleton.get(SizeValidPlugin.class);
    }
}

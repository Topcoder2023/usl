package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.PastOrPresent;
import com.gitee.usl.plugin.impl.PastOrPresentValidPlugin;

/**
 * 时间校验注解函数增强器
 * 将时间校验注解校验器插件安装到指定函数中
 *
 * @author jiahao.song
 */
public class PastOrPresentValidEnhancer extends AbstractValidEnhancer<PastOrPresent> {

    @Override
    protected Plugin provide() {
        return Singleton.get(PastOrPresentValidPlugin.class);
    }
}

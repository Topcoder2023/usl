package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Digits;
import com.gitee.usl.plugin.impl.DigitsValidPlugin;

/**
 * 数字格式注解函数增强器
 * 将数字格式注解校验器插件安装到指定函数中
 * @author jingshu.zeng
 */
public class DigitsValidEnhancer extends AbstractValidEnhancer<Digits> {

    @Override
    protected Plugin provide() {
        return Singleton.get(DigitsValidPlugin.class);
    }
}

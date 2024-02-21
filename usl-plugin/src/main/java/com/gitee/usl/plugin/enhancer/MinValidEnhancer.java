package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Min;
import com.gitee.usl.plugin.impl.MinValidPlugin;

/**
 * 最小值注解函数增强器
 * 将最小值注解校验器插件安装到指定函数中
 *
 * @author jingshu.zeng
 */
public class MinValidEnhancer extends AbstractValidEnhancer<Min> {

    @Override
    protected Plugin provide() {
        return Singleton.get(MinValidPlugin.class);
    }
}
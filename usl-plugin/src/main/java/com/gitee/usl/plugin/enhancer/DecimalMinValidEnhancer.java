package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.DecimalMin;
import com.gitee.usl.plugin.impl.DecimalMinValidPlugin;

/**
 * DecimalMin注解函数增强器
 *
 * @author jingshu.zeng
 */
public class DecimalMinValidEnhancer extends AbstractValidEnhancer<DecimalMin> {

    @Override
    protected Plugin provide() {
        return Singleton.get(DecimalMinValidPlugin.class);
    }
}

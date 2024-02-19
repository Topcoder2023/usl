package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.DecimalMax;
import com.gitee.usl.plugin.impl.DecimalMaxValidPlugin;

/**
 * DecimalMax注解函数增强器
 *
 * @author jingshu.zeng
 */
public class DecimalMaxValidEnhancer extends AbstractValidEnhancer<DecimalMax> {

    @Override
    protected Plugin provide() {
        return Singleton.get(DecimalMaxValidPlugin.class);
    }
}

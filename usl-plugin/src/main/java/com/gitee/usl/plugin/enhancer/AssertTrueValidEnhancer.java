package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.AssertTrue;
import com.gitee.usl.plugin.impl.AssertTrueValidPlugin;

/**
 * 真值校验注解函数增强器
 *
 * @author jingshu.zeng
 */
public class AssertTrueValidEnhancer extends AbstractValidEnhancer<AssertTrue> {

    @Override
    protected Plugin provide() {
        return Singleton.get(AssertTrueValidPlugin.class);
    }
}

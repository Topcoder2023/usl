package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.AssertFalse;
import com.gitee.usl.plugin.impl.AssertFalseValidPlugin;

/**
 * 假值校验注解函数增强器
 * @author jingshu.zeng
 */
public class AssertFalseValidEnhancer extends AbstractValidEnhancer<AssertFalse> {

    @Override
    protected Plugin provide() {
        return Singleton.get(AssertFalseValidPlugin.class);
    }
}

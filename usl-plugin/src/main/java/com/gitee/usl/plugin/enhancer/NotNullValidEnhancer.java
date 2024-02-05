package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.NotNull;
import com.gitee.usl.plugin.impl.NotNullValidPlugin;

/**
 * @author hongda.li
 */
public class NotNullValidEnhancer extends AbstractValidEnhancer<NotNull> {
    @Override
    protected Plugin provide() {
        return Singleton.get(NotNullValidPlugin.class);
    }
}

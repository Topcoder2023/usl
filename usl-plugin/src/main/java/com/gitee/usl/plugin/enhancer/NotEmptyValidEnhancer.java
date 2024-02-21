package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.NotEmpty;
import com.gitee.usl.plugin.impl.NotEmptyValidPlugin;

/**
 * @author hongda.li
 */
public class NotEmptyValidEnhancer extends AbstractValidEnhancer<NotEmpty> {
    @Override
    protected Plugin provide() {
        return Singleton.get(NotEmptyValidPlugin.class);
    }
}

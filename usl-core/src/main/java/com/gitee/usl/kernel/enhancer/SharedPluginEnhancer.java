package com.gitee.usl.kernel.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.FunctionPluggable;
import com.gitee.usl.kernel.plugin.SharedPlugin;

/**
 * @author hongda.li
 */
public class SharedPluginEnhancer extends AbstractFunctionEnhancer {
    @Override
    protected void enhancePluggable(FunctionPluggable fp) {
        fp.plugins().install(Singleton.get(SharedPlugin.class));
    }
}

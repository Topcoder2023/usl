package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Future;
import com.gitee.usl.plugin.impl.FutureValidPlugin;


/**
 * 未来日期验证插件增强器
 * 将未来日期验证插件安装到指定函数中
 *
 * @author jingshu.zeng
 */
public class FutureValidEnhancer extends AbstractValidEnhancer<Future> {

    @Override
    protected Plugin provide() {
        return Singleton.get(FutureValidPlugin.class);
    }
}

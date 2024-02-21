package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.FutureOrPresent;
import com.gitee.usl.plugin.impl.FutureOrPresentValidPlugin;


/**
 * 未来日期或现在日期验证插件增强器
 * 将未来日期或现在日期验证插件安装到指定函数中
 *
 * @author jingshu.zeng
 */
public class FutureOrPresentValidEnhancer extends AbstractValidEnhancer<FutureOrPresent> {

    @Override
    protected Plugin provide() {
        return Singleton.get(FutureOrPresentValidPlugin.class);
    }
}

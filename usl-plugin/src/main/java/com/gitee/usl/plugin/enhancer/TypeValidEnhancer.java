package com.gitee.usl.plugin.enhancer;

import cn.hutool.core.lang.Singleton;
import com.gitee.usl.api.plugin.Plugin;
import com.gitee.usl.plugin.annotation.Type;
import com.gitee.usl.plugin.impl.TypeValidPlugin;

/**
 * 参数类型校验函数增强器
 * 将参数类型校验插件安装到指定函数中
 *
 * @author jingshu.zeng
 */
public class TypeValidEnhancer extends AbstractValidEnhancer<Type> {

    @Override
    protected Plugin provide() {
        return Singleton.get(TypeValidPlugin.class);
    }
}

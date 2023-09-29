package com.gitee.usl.kernel.engine;

import com.gitee.usl.kernel.configure.EngineConfiguration;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.List;

/**
 * @author hongda.li
 */
public interface FunctionProvider {

    /**
     * 通过脚本引擎配置提供函数定义信息
     *
     * @param configuration 脚本引擎配置
     * @return 函数定义信息集合
     */
    List<AviatorFunction> provide(EngineConfiguration configuration);
}

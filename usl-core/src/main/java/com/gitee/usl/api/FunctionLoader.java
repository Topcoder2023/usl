package com.gitee.usl.api;

import com.gitee.usl.kernel.configure.EngineConfig;
import com.googlecode.aviator.runtime.type.AviatorFunction;

import java.util.List;

/**
 * @author hongda.li
 */
public interface FunctionLoader {

    /**
     * 通过脚本引擎配置提供函数定义信息
     *
     * @param configuration 脚本引擎配置
     * @return 函数定义信息集合
     */
    List<AviatorFunction> load(EngineConfig configuration);
}

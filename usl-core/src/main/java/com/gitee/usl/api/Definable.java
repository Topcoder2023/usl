package com.gitee.usl.api;

import com.gitee.usl.kernel.engine.FunctionDefinition;

/**
 * 可定义化的
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface Definable {
    /**
     * 函数定义
     *
     * @return 定义信息
     */
    FunctionDefinition definition();
}

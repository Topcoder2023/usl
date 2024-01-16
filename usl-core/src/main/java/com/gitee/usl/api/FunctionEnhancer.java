package com.gitee.usl.api;

import com.gitee.usl.grammar.runtime.type.Function;

/**
 * USL 函数增强器
 * 对已注册成功的函数进行增强
 * 如安装插件等
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface FunctionEnhancer {
    /**
     * 对已注册函数增强
     *
     * @param function 函数实例
     */
    void enhance(Function function);
}

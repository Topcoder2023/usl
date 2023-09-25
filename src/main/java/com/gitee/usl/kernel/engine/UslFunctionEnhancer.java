package com.gitee.usl.kernel.engine;

/**
 * USL 函数增强器
 * 对已注册成功的函数进行增强
 * 如安装插件等
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface UslFunctionEnhancer {
    /**
     * 对已注册函数增强
     *
     * @param definition 函数定义信息
     */
    void enhance(FunctionDefinition definition);
}

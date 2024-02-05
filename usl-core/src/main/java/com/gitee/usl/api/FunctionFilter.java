package com.gitee.usl.api;

import com.gitee.usl.grammar.runtime.type._Function;

/**
 * 函数过滤器
 *
 * @author hongda.li
 */
public interface FunctionFilter {

    /**
     * 执行过滤逻辑
     * 被过滤的函数不会被注册
     *
     * @param function 函数示例
     * @return 是否允许注册
     */
    boolean allowedRegister(_Function function);
}

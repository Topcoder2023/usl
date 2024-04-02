package com.manda.usl.kernel.common;

import com.manda.usl.kernel.function.EnhancedFunction;

/**
 * 函数增强器
 *
 * @author hongda.li
 */
public interface FunctionEnhancer {

    /**
     * 对函数进行功能扩展
     *
     * @param function 待增强函数
     */
    void enhance(EnhancedFunction function);
}

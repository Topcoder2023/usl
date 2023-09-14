package com.gitee.usl.kernel.engine;

import com.googlecode.aviator.runtime.type.AviatorFunction;

/**
 * USL 函数工厂
 * 负责生产各类 USL 函数
 *
 * @author hongda.li
 */
public interface UslFunctionFactory {
    /**
     * 是否支持该函数定义
     *
     * @param definition 函数定义信息
     * @return 是否支持
     */
    boolean supported(UslFunctionDefinition definition);

    /**
     * 根据函数定义创建 Aviator 函数
     *
     * @param definition 函数定义
     * @return Aviator 函数
     */
    AviatorFunction create(UslFunctionDefinition definition);
}

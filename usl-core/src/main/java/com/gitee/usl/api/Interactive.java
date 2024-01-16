package com.gitee.usl.api;

import com.gitee.usl.Runner;

/**
 * 交互接口
 *
 * @author hongda.li
 */
@FunctionalInterface
public interface Interactive {
    /**
     * 开启交互
     *
     * @param runner USL 执行器实例
     */
    void open(Runner runner);
}

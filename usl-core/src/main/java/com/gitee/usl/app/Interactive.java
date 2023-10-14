package com.gitee.usl.app;

import com.gitee.usl.USLRunner;

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
    void open(USLRunner runner);
}

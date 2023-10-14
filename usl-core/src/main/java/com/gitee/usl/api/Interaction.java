package com.gitee.usl.api;

import com.gitee.usl.USLRunner;

/**
 * 交互接口
 *
 * @author hongda.li
 */
public interface Interaction {
    /**
     * 开启交互
     *
     * @param runner USL 执行器
     */
    void start(USLRunner runner);
}

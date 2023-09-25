package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
public interface UslFailurePlugin extends UslPlugin {
    /**
     * 函数执行失败回调函数
     *
     * @param session 函数调用会话
     */
    void onFailure(FunctionSession session);
}

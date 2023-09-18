package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslFinallyPlugin extends UslPlugin {
    /**
     * 函数执行完成时的回调函数
     *
     * @param session 函数调用会话
     */
    void onFinally(UslFunctionSession session);
}

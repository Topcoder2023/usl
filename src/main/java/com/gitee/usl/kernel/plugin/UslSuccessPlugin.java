package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslSuccessPlugin extends UslPlugin {
    /**
     * 函数执行成功时的回调函数
     *
     * @param session 函数调用会话
     */
    void onSuccess(UslFunctionSession session);
}

package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
public interface SuccessPlugin extends Plugin {
    /**
     * 函数执行成功时的回调函数
     *
     * @param session 函数调用会话
     */
    void onSuccess(FunctionSession session);
}

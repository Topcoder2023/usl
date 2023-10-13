package com.gitee.usl.api.plugin;

import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
public interface FailurePlugin extends Plugin {
    /**
     * 函数执行失败回调函数
     *
     * @param session 函数调用会话
     */
    void onFailure(FunctionSession session);
}

package com.manda.usl.kernel.plugin;

import com.manda.usl.kernel.view.FunctionSession;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface FailurePlugin extends Plugin {
    /**
     * 函数执行失败回调函数
     *
     * @param session 函数调用会话
     */
    void onFailure(FunctionSession session);
}

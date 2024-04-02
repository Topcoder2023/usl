package com.manda.usl.kernel.plugin;

import com.manda.usl.kernel.entity.FunctionSession;

/**
 * @author hongda.li
 */
public interface FinallyPlugin extends Plugin {
    /**
     * 函数执行完成时的回调函数
     *
     * @param session 函数调用会话
     */
    void onFinally(FunctionSession session);
}

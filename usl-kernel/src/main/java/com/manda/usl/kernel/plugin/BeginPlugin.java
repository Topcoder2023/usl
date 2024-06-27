package com.manda.usl.kernel.plugin;

import com.manda.usl.kernel.view.FunctionSession;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface BeginPlugin extends Plugin {
    /**
     * 开始执行之前回调函数
     *
     * @param session 函数调用会话
     */
    void onBegin(FunctionSession session);
}

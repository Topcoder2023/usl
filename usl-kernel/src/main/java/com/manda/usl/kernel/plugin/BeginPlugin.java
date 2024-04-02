package com.manda.usl.kernel.plugin;

import com.manda.usl.kernel.entity.FunctionSession;

/**
 * @author hongda.li
 */
public interface BeginPlugin extends Plugin {
    /**
     * 开始执行之前回调函数
     *
     * @param session 函数调用会话
     */
    void onBegin(FunctionSession session);
}

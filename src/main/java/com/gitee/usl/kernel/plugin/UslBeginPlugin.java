package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslBeginPlugin extends UslPlugin {
    /**
     * 开始执行之前回调函数
     *
     * @param session 函数调用会话
     */
    void onBegin(UslFunctionSession session);
}

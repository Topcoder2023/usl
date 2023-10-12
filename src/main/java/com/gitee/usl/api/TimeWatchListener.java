package com.gitee.usl.api;

import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
@FunctionalInterface
public interface TimeWatchListener {
    /**
     * 本次执行的会话
     *
     * @param session   会话信息
     * @param totalTime 执行的总纳秒
     */
    void onListen(FunctionSession session, long totalTime);
}

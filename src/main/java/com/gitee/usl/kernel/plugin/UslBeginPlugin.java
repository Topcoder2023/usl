package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslBeginPlugin extends UslPlugin {
    /**
     * 开始执行之前
     */
    void onBegin(UslFunctionSession session);
}

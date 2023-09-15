package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslFinallyPlugin extends UslPlugin {

    void onFinally(UslFunctionSession session);
}

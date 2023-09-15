package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslFailurePlugin extends UslPlugin {

    void onFailure(UslFunctionSession session);
}

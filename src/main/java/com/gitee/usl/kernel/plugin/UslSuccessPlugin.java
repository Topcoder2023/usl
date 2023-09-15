package com.gitee.usl.kernel.plugin;

import com.gitee.usl.kernel.engine.UslFunctionSession;

/**
 * @author hongda.li
 */
public interface UslSuccessPlugin extends UslPlugin {

    void onSuccess(UslFunctionSession session);
}

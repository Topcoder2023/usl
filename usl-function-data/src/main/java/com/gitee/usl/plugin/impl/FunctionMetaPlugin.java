package com.gitee.usl.plugin.impl;

import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
public class FunctionMetaPlugin implements SuccessPlugin, FailurePlugin, FinallyPlugin {
    @Override
    public void onFailure(FunctionSession session) {

    }

    @Override
    public void onSuccess(FunctionSession session) {

    }

    @Override
    public void onFinally(FunctionSession session) {

    }
}

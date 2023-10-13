package com.gitee.usl.app.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
public class ValidatorPlugin implements BeginPlugin, SuccessPlugin, FailurePlugin {
    @Override
    public void onBegin(FunctionSession session) {

    }

    @Override
    public void onFailure(FunctionSession session) {

    }

    @Override
    public void onSuccess(FunctionSession session) {

    }
}

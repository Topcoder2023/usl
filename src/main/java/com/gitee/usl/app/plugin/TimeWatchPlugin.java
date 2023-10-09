package com.gitee.usl.app.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * 执行时长监控插件
 *
 * @author hongda.li
 */
public class TimeWatchPlugin implements BeginPlugin, SuccessPlugin, FailurePlugin {
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

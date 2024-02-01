package com.gitee.usl.kernel.plugin;

import com.gitee.usl.api.annotation.Order;
import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * 共享变量插件
 *
 * @author hongda.li
 */
@Order(Integer.MIN_VALUE)
public class SharedPlugin implements BeginPlugin, FinallyPlugin {
    @Override
    public void onBegin(FunctionSession session) {
        SharedSession.setSession(session);
    }

    @Override
    public void onFinally(FunctionSession session) {
        SharedSession.clear();
    }
}

package com.gitee.usl.app.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * 权限插件
 * 对没有权限的会话进行拦截
 *
 * @author hongda.li
 */
public class PermissionPlugin implements BeginPlugin {
    @Override
    public void onBegin(FunctionSession session) {
        // 对没有权限的会话进行拦截
    }
}

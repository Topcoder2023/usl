package com.gitee.usl.app.plugin;

import com.gitee.usl.api.plugin.FailurePlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * 重试插件
 * 当执行失败时，按照指定的重试次数进行重试
 *
 * @author hongda.li
 */
public class RetryPlugin implements FailurePlugin {
    @Override
    public void onFailure(FunctionSession session) {

    }
}

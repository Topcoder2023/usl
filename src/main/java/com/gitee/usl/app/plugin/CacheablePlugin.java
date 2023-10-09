package com.gitee.usl.app.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.api.plugin.SuccessPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

/**
 * @author hongda.li
 */
public class CacheablePlugin implements BeginPlugin, SuccessPlugin {
    @Override
    public void onBegin(FunctionSession session) {
        // 1.生成唯一的缓存键

        // 2.从缓存里面根据key查询

        // 3.如果缓存有结果则将结果直接保存到session里面返回

        // 4.如果没有缓存则跳过
    }

    @Override
    public void onSuccess(FunctionSession session) {
        // 如果开启了缓存则把执行结果保存到session里面
    }
}

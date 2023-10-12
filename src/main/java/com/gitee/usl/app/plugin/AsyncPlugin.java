package com.gitee.usl.app.plugin;

import com.gitee.usl.api.plugin.BeginPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步插件
 *
 * @author hongda.li
 */
public class AsyncPlugin implements BeginPlugin {
    private static final Object RESULT_PLACEHOLDER = new Object() {
        @Override
        public String toString() {
            return "[Async_Call_Result]";
        }
    };
    private final ThreadPoolExecutor executor;

    public AsyncPlugin(ThreadPoolExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void onBegin(FunctionSession session) {
        executor.execute(() -> session.invocation().invoke());

        // 此处设置非空返回值
        // 目的是避免重复调用
        session.setResult(RESULT_PLACEHOLDER);
    }
}

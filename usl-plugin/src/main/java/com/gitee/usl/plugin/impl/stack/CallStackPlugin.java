package com.gitee.usl.plugin.impl.stack;

import com.gitee.usl.Runner;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.api.plugin.FinallyPlugin;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.grammar.ScriptEngine;

/**
 * @author hongda.li
 */
@Description("调用栈插件")
public class CallStackPlugin implements FinallyPlugin {

    @Description("USL执行器")
    private final Runner runner;

    public CallStackPlugin(Runner runner) {
        this.runner = runner;

        @Description("Aviator引擎实例")
        ScriptEngine instance = this.runner.getConfiguration()
                .getEngineConfig()
                .getEngineInitializer()
                .getInstance();

        if (instance.getEnvProcessor() == null) {
            instance.setEnvProcessor(new EnvProcessorImpl(runner.getName()));
        }
    }

    @Override
    public void onFinally(FunctionSession session) {
        CallStack.push(runner.getName(), session);
    }

}

package com.gitee.usl.plugin.impl.stack;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.api.ScriptProcessor;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.utils.Env;

import java.util.Map;

/**
 * @author hongda.li
 */
@Description("上下文环境处理器")
public record EnvProcessorImpl(@Description("USL执行器的名称") String runnerName) implements ScriptProcessor {

    public EnvProcessorImpl(String runnerName) {
        this.runnerName = runnerName;
    }

    @Override
    public void onBegin(Map<String, Object> env, Script script) {
        CallStack.push(runnerName, new FunctionSession((Env) env, null, null));
    }

    @Override
    public void onAfter(Map<String, Object> env, Script script) {
        CallStack.clear(runnerName);
    }

}

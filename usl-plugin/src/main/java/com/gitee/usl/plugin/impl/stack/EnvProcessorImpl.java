package com.gitee.usl.plugin.impl.stack;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.kernel.engine.FunctionSession;
import com.gitee.usl.grammar.ScriptProcessor;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.utils.Env;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

/**
 * @author hongda.li
 */
@Getter
@ToString
@Description("上下文环境处理器")
public class EnvProcessorImpl implements ScriptProcessor {

    @Description("USL执行器的名称")
    private final String runnerName;

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

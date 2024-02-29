package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.domain.ExecutableParam;
import com.gitee.usl.domain.FileParam;
import com.gitee.usl.domain.ResourceParam;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.infra.structure.SharedSession;
import com.gitee.usl.kernel.engine.FunctionSession;

import java.io.File;

/**
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class ScriptFunction {
    @Function("script_file")
    public ExecutableParam script(File file) {
        FunctionSession session = SharedSession.getSession();
        USLRunner runner = session.getDefinition().getRunner();
        Env env = session.getEnv();
        ExecutableParam param = new ExecutableParam(runner, new FileParam(file));
        param.addContext(env);
        return param;
    }

    @Function("script_resource")
    public ExecutableParam script(String name) {
        FunctionSession session = SharedSession.getSession();
        USLRunner runner = session.getDefinition().getRunner();
        Env env = session.getEnv();
        ExecutableParam param = new ExecutableParam(runner, new ResourceParam(name));
        param.addContext(env);
        return param;
    }

    @Function("script_execute")
    public Object run(ExecutableParam param) {
        return param.execute();
    }

    @Function("script_result")
    public Object result(ExecutableParam script) {
        return script.getCacheResult();
    }
}

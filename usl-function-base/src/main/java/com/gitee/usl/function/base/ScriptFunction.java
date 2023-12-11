package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.structure.Script;
import com.googlecode.aviator.utils.Env;

/**
 * @author hongda.li
 */
@SuppressWarnings("unused")
@FunctionGroup
public class ScriptFunction {
    @Function("script")
    public Script script(USLRunner runner, String path) {
        return new Script(runner, path);
    }

    @Function("script_path")
    public String path(Script script) {
        return script.getPath();
    }

    @Function("script_content")
    public String content(Script script) {
        return script.getContent();
    }

    @Function("script_run")
    public Object run(Env env, Script script) {
        return script.run(env);
    }

    @Function("script_result")
    public Object result(Env env, Script script) {
        return script.getResult(env);
    }
}

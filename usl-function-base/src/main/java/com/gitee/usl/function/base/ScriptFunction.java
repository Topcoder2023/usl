package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.infra.structure.Script;
import com.googlecode.aviator.utils.Env;

/**
 * @author hongda.li
 */
@Func
public class ScriptFunction {
    @Func("script")
    public Script script(USLRunner runner, String path) {
        return new Script(runner, path);
    }

    @Func("script_path")
    public String path(Script script) {
        return script.getPath();
    }

    @Func("script_content")
    public String content(Script script) {
        return script.getContent();
    }

    @Func("script_run")
    public Object run(Env env, Script script) {
        return script.run(env);
    }

    @Func("script_result")
    public Object result(Env env, Script script) {
        return script.getResult(env);
    }
}

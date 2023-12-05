package com.gitee.usl.function.base;

import com.gitee.usl.api.annotation.Func;
import com.gitee.usl.function.base.entity.Script;

/**
 * @author hongda.li
 */
@Func
public class ScriptFunction {

    @Func("script")
    public Script script(String path) {
        return new Script(path);
    }
}

package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class WithMetaFunction extends BasicFunction {

    public static final String NAME = "with_meta";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject[] arguments) {
        return arguments[0].withMeta(arguments[1].getValue(env), arguments[2].getValue(env));
    }

}

package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemComponent;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemComponent
public class WithMetaFunction extends BasicFunction {

    public static final String NAME = "with_meta";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {
        arguments[0].getMetadata().putAny(arguments[1].getValue(env), arguments[2].getValue(env));
        return arguments[0];
    }

}

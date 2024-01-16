package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.runtime.type._RuntimeJavaType;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class ReducerReturnFunction extends BasicFunction {

    public static final String NAME = "__reducer_return";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {
        return ReducerResult.withReturn(_RuntimeJavaType.valueOf(arguments[0].getValue(env)));
    }

}

package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemComponent;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Null;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemComponent
public class ReducerBreakFunction extends BasicFunction {

    public static final String NAME = "__reducer_break";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {
        return ReducerResult.withBreak(_Null.NIL);
    }

}

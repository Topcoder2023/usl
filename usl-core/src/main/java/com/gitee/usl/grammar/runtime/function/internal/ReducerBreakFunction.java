package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorNil;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class ReducerBreakFunction extends BasicFunction {

    public static final String NAME = "__reducer_break";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject[] arguments) {
        return ReducerResult.withBreak(AviatorNil.NIL);
    }

}

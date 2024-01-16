package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type.Function;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemFunction
public class IfCallccFunction extends BasicFunction {

    public static final String NAME = "__if_callcc";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public AviatorObject execute(Env env, AviatorObject[] arguments) {
        if (arguments[0] instanceof ReducerResult) {
            return arguments[0];
        } else {
            final Object nextClauseVal = arguments[1].getValue(env);
            if ((nextClauseVal instanceof ReducerResult)
                    && ((ReducerResult) nextClauseVal).isEmptyState()) {
                return arguments[0];
            }

            Function otherClausesFn = (Function) nextClauseVal;
            try {
                AviatorObject result = otherClausesFn.execute(env);
                // No remaining statements, return the if statement result.
                if ((result instanceof ReducerResult) && ((ReducerResult) result).isEmptyState()) {
                    return arguments[0];
                }
                return result;
            } finally {
                RuntimeUtils.resetLambdaContext(otherClausesFn);
            }
        }
    }

}

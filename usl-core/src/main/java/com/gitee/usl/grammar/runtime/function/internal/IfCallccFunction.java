package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemComponent;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
@SystemComponent
public class IfCallccFunction extends BasicFunction {

    public static final String NAME = "__if_callcc";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {
        if (arguments[0] instanceof ReducerResult) {
            return arguments[0];
        } else {
            final Object nextClauseVal = arguments[1].getValue(env);
            if ((nextClauseVal instanceof ReducerResult)
                    && ((ReducerResult) nextClauseVal).isEmptyState()) {
                return arguments[0];
            }

            _Function otherClausesFn = (_Function) nextClauseVal;
            try {
                _Object result = otherClausesFn.execute(env);
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

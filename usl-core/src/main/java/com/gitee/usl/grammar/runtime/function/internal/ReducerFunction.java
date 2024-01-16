package com.gitee.usl.grammar.runtime.function.internal;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type._Long;
import com.gitee.usl.grammar.runtime.type._RuntimeJavaType;
import com.gitee.usl.grammar.runtime.type._Range;
import com.gitee.usl.grammar.Options;
import com.gitee.usl.grammar.utils.Env;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.runtime.type._Function;
import com.gitee.usl.grammar.runtime.type._Null;
import com.gitee.usl.grammar.runtime.type._Object;
import com.gitee.usl.grammar.utils.Constants;

/**
 * @author hongda.li
 */
@SystemFunction
public class ReducerFunction extends BasicFunction {

    public static final String NAME = "__reducer_callcc";

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public _Object execute(Env env, _Object[] arguments) {
        Object coll = arguments[0].getValue(env);
        _Function iteratorFn = (_Function) arguments[1];

        try {
            return reduce(env, arguments[1], arguments[2], coll, iteratorFn);
        } finally {
            RuntimeUtils.resetLambdaContext(iteratorFn);
        }
    }

    private _Object reduce(final Env env, final _Object arg2,
                           final _Object arg3, Object coll, _Function iteratorFn) {
        int maxLoopCount = RuntimeUtils.getInstance(env).getOptionValue(Options.MAX_LOOP_COUNT).number;
        _Object result = _Null.NIL;
        long c = 0;

        if (coll != _Range.LOOP) {
            long arities = (long) arg2.meta(Constants.ARITIES_META);
            long index = 0;
            boolean unboxEntry =
                    arities == 2 && coll != null && Map.class.isAssignableFrom(coll.getClass());

            for (Object obj : RuntimeUtils.seq(coll, env)) {
                if (arities == 1) {
                    result = iteratorFn.execute(env, new _Object[]{_RuntimeJavaType.valueOf(obj)});
                } else {
                    if (unboxEntry) {
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        result = iteratorFn.execute(env, new _Object[]{_RuntimeJavaType.valueOf(entry.getKey()),
                                _RuntimeJavaType.valueOf(entry.getValue())});
                    } else {
                        result = iteratorFn.execute(env, new _Object[]{_Long.valueOf(index++),
                                _RuntimeJavaType.valueOf(obj)});
                    }
                }
                if (!(result instanceof ReducerResult midResult)) {
                    continue;
                }

                boolean breakOut = false;
                result = midResult.obj;

                if (midResult.state == ReducerState.Empty) {
                    continue;
                }
                switch (midResult.state) {
                    case Break:
                        breakOut = true;
                        break;
                    case Return:
                        return midResult;
                    default:
                        break;
                }
                if (breakOut) {
                    break;
                }
            }
        } else {
            // while statement
            while (true) {
                if (maxLoopCount > 0 && ++c > maxLoopCount) {
                    throw new ExpressionRuntimeException("Overflow max loop count: " + maxLoopCount);
                }
                result = iteratorFn.execute(env, new _Object[]{});
                if (!(result instanceof ReducerResult midResult)) {
                    continue;
                }
                boolean breakOut = false;
                result = midResult.obj;

                if (midResult.state == ReducerState.Empty) {
                    continue;
                }
                switch (midResult.state) {
                    case Break:
                        breakOut = true;
                        break;
                    case Return:
                        return midResult;
                    default:
                        break;
                }
                if (breakOut) {
                    break;
                }
            }
        }

        Object contObj = arg3.getValue(env);
        if ((contObj instanceof ReducerResult) && ((ReducerResult) contObj).isEmptyState()) {
            return result;
        }

        _Object contResult = ((_Function) contObj).execute(env, new _Object[]{});
        if ((contResult instanceof ReducerResult) && ((ReducerResult) contResult).isEmptyState()) {
            // empty continuation, return current result.
            return result;
        } else {
            return contResult;
        }
    }
}

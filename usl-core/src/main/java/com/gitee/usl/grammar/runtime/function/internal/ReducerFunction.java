package com.gitee.usl.grammar.runtime.function.internal;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.BasicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorLong;
import com.gitee.usl.grammar.runtime.type.AviatorRuntimeJavaType;
import com.gitee.usl.grammar.runtime.type.Range;
import com.gitee.usl.grammar.Options;
import com.gitee.usl.grammar.utils.Env;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.runtime.type.Function;
import com.gitee.usl.grammar.runtime.type.AviatorNil;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
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
    public AviatorObject execute(Env env, AviatorObject[] arguments) {
        Object coll = arguments[0].getValue(env);
        Function iteratorFn = (Function) arguments[1];

        try {
            return reduce(env, arguments[1], arguments[2], coll, iteratorFn);
        } finally {
            RuntimeUtils.resetLambdaContext(iteratorFn);
        }
    }

    private AviatorObject reduce(final Env env, final AviatorObject arg2,
                                 final AviatorObject arg3, Object coll, Function iteratorFn) {
        int maxLoopCount = RuntimeUtils.getInstance(env).getOptionValue(Options.MAX_LOOP_COUNT).number;
        AviatorObject result = AviatorNil.NIL;
        long c = 0;

        if (coll != Range.LOOP) {
            long arities = (long) arg2.meta(Constants.ARITIES_META);
            long index = 0;
            boolean unboxEntry =
                    arities == 2 && coll != null && Map.class.isAssignableFrom(coll.getClass());

            for (Object obj : RuntimeUtils.seq(coll, env)) {
                if (arities == 1) {
                    result = iteratorFn.execute(env, new AviatorObject[]{AviatorRuntimeJavaType.valueOf(obj)});
                } else {
                    if (unboxEntry) {
                        Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
                        result = iteratorFn.execute(env, new AviatorObject[]{AviatorRuntimeJavaType.valueOf(entry.getKey()),
                                AviatorRuntimeJavaType.valueOf(entry.getValue())});
                    } else {
                        result = iteratorFn.execute(env, new AviatorObject[]{AviatorLong.valueOf(index++),
                                AviatorRuntimeJavaType.valueOf(obj)});
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
                result = iteratorFn.execute(env, new AviatorObject[]{});
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

        AviatorObject contResult = ((Function) contObj).execute(env, new AviatorObject[]{});
        if ((contResult instanceof ReducerResult) && ((ReducerResult) contResult).isEmptyState()) {
            // empty continuation, return current result.
            return result;
        } else {
            return contResult;
        }
    }
}

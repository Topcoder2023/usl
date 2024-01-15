package com.googlecode.aviator.runtime.function.internal;

import java.util.Map;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.type.USLFunction;
import com.googlecode.aviator.runtime.type.AviatorLong;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.Range;
import com.googlecode.aviator.utils.Constants;

/**
 * Internal reducer-callcc function for 'for-loop' structure.
 *
 * @since 5.0.0
 * @author dennis(killme2008@gmail.com)
 *
 */
public class ReducerFunction extends AbstractFunction {

  private static final long serialVersionUID = -6117602709327741955L;

  private ReducerFunction() {}

  public static final ReducerFunction INSTANCE = new ReducerFunction();

  @Override
  public String getName() {
    return "__reducer_callcc";
  }

  @Override
  public final USLObject call(final Map<String, Object> env, final USLObject arg1,
                              final USLObject arg2, final USLObject arg3) {

    Object coll = arg1.getValue(env);
    USLFunction iteratorFn = (USLFunction) arg2;

    try {
      return reduce(env, arg2, arg3, coll, iteratorFn);
    } finally {
      RuntimeUtils.resetLambdaContext(iteratorFn);
    }
  }

  private USLObject reduce(final Map<String, Object> env, final USLObject arg2,
                           final USLObject arg3, Object coll, USLFunction iteratorFn) {
    int maxLoopCount = RuntimeUtils.getInstance(env).getOptionValue(Options.MAX_LOOP_COUNT).number;
    USLObject result = AviatorNil.NIL;
    long c = 0;

    if (coll != Range.LOOP) {
      long arities = (long) arg2.meta(Constants.ARITIES_META);
      long index = 0;
      boolean unboxEntry =
          arities == 2 && coll != null && Map.class.isAssignableFrom(coll.getClass());
      // for..in loop
      for (Object obj : RuntimeUtils.seq(coll, env)) {
        if (arities == 1) {
          result = iteratorFn.call(env, AviatorRuntimeJavaType.valueOf(obj));
        } else {
          if (unboxEntry) {
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;
            result = iteratorFn.call(env, AviatorRuntimeJavaType.valueOf(entry.getKey()),
                AviatorRuntimeJavaType.valueOf(entry.getValue()));
          } else {
            result = iteratorFn.call(env, AviatorLong.valueOf(index++),
                AviatorRuntimeJavaType.valueOf(obj));
          }
        }
        if (!(result instanceof ReducerResult)) {
          continue;
        }

        boolean breakOut = false;
        ReducerResult midResult = (ReducerResult) result;
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
        result = iteratorFn.call(env);
        if (!(result instanceof ReducerResult)) {
          continue;
        }
        boolean breakOut = false;
        ReducerResult midResult = (ReducerResult) result;
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

    USLObject contResult = ((USLFunction) contObj).call(env);
    if ((contResult instanceof ReducerResult) && ((ReducerResult) contResult).isEmptyState()) {
      // empty continuation, return current result.
      return result;
    } else {
      return contResult;
    }
  }
}

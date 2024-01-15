package com.googlecode.aviator.runtime.function.internal;

import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.type.USLFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.utils.Reflector;

import java.util.List;
import java.util.Map;

/**
 * __try(try_func, catch_handlers, finally_fn, callcc)
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class TryCatchFunction extends AbstractFunction {

  private static final long serialVersionUID = 7314510329619948965L;

  private TryCatchFunction() {}

  public static final TryCatchFunction INSTANCE = new TryCatchFunction();

  @Override
  public String getName() {
    return "__try";
  }

  @SuppressWarnings("unchecked")
  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4) {
    USLFunction tryBody = (USLFunction) arg1;
    List<CatchHandler> catchHandlers = (List<CatchHandler>) arg2.getValue(env);
    USLFunction finallyBody = arg3 != AviatorNil.NIL ? (USLFunction) arg3 : null;
    USLObject result = null;
    try {
      result = tryBody.call(env);
    } catch (Throwable t) {
      boolean handle = false;
      if (catchHandlers != null) {
        for (CatchHandler handler : catchHandlers) {
          if (handler.isMatch(t.getClass())) {
            USLObject ret = handler.getFunc().call(env, AviatorRuntimeJavaType.valueOf(t));
            result = chooseResult(result, ret);
            handle = true;
            break;
          }
        }
      }
      if (!handle) {
        throw Reflector.sneakyThrow(t);
      }
    } finally {
      try {
        if (finallyBody != null) {
          USLObject ret = finallyBody.call(env);
          result = chooseResult(result, ret);
        }
      } finally {
        RuntimeUtils.resetLambdaContext(tryBody);
        RuntimeUtils.resetLambdaContext(finallyBody);
      }
    }

    if (isReturnResult(result)) {
      return result;
    }

    Object val = arg4.getValue(env);
    if ((val instanceof ReducerResult) && ((ReducerResult) val).isEmptyState()) {
      return result;
    }
    USLFunction continueFn = (USLFunction) val;
    try {
      USLObject contResult = continueFn.call(env);
      if ((contResult instanceof ReducerResult) && ((ReducerResult) contResult).isEmptyState()) {
        return result;
      } else {
        return contResult;
      }
    } finally {
      RuntimeUtils.resetLambdaContext(continueFn);
    }
  }

  public USLObject chooseResult(final USLObject result, final USLObject ret) {
    if (result instanceof ReducerResult) {
      if (ret instanceof ReducerResult && isNewState(result, ret)) {
        return ret;
      }
      return result;
    } else {
      return ret;
    }
  }

  private boolean isNewState(final USLObject result, final USLObject ret) {
    return ((ReducerResult) ret).state.compareTo(((ReducerResult) result).state) >= 0;
  }

  private boolean isReturnResult(final USLObject ret) {
    return ret instanceof ReducerResult && ((ReducerResult) ret).state == ReducerState.Return;
  }

}

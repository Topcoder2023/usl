package com.googlecode.aviator.runtime.function.internal;

import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.type.USLFunction;
import com.gitee.usl.grammar.type.USLObject;

import java.util.Map;

/**
 * __if_callcc function
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class IfCallccFunction extends AbstractFunction {

  private static final long serialVersionUID = 3511688119189694245L;

  private IfCallccFunction() {

  }

  public static final IfCallccFunction INSTANCE = new IfCallccFunction();

  @Override
  public String getName() {
    return "__if_callcc";
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2) {
    if (arg1 instanceof ReducerResult) {
      return arg1;
    } else {
      final Object nextClauseVal = arg2.getValue(env);
      if ((nextClauseVal instanceof ReducerResult)
          && ((ReducerResult) nextClauseVal).isEmptyState()) {
        return arg1;
      }

      USLFunction otherClausesFn = (USLFunction) nextClauseVal;
      try {
        USLObject result = otherClausesFn.call(env);
        // No remaining statements, return the if statement result.
        if ((result instanceof ReducerResult) && ((ReducerResult) result).isEmptyState()) {
          return arg1;
        }
        return result;
      } finally {
        RuntimeUtils.resetLambdaContext(otherClausesFn);
      }
    }
  }
}

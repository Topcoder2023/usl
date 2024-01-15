package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

import java.util.Map;

/**
 * __if_callcc function
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
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
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2) {
    if (arg1 instanceof ReducerResult) {
      return arg1;
    } else {
      final Object nextClauseVal = arg2.getValue(env);
      if ((nextClauseVal instanceof ReducerResult)
          && ((ReducerResult) nextClauseVal).isEmptyState()) {
        return arg1;
      }

      AviatorFunction otherClausesFn = (AviatorFunction) nextClauseVal;
      try {
        AviatorObject result = otherClausesFn.call(env);
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

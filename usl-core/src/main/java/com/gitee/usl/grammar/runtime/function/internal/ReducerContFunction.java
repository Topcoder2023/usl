package com.gitee.usl.grammar.runtime.function.internal;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorNil;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

/**
 * Internal reducer-continue function for 'for-loop' structure.
 *
 * @since 5.0.0
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
public class ReducerContFunction extends AbstractFunction {


  private ReducerContFunction() {

  }

  public static final ReducerContFunction INSTANCE = new ReducerContFunction();

  private static final long serialVersionUID = 7517333105872722540L;

  @Override
  public String getName() {
    return "__reducer_cont";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {
    return ReducerResult.withCont(AviatorNil.NIL);
  }

}

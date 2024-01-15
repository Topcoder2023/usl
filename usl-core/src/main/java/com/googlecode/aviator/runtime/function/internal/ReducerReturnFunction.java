package com.googlecode.aviator.runtime.function.internal;

import java.util.Map;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

/**
 * Internal reducer-return function for 'for-loop' structure.
 *
 * @since 5.0.0
 * @author dennis(killme2008@gmail.com)
 *
 */
public class ReducerReturnFunction extends AbstractFunction {

  private ReducerReturnFunction() {}

  public static final ReducerReturnFunction INSTANCE = new ReducerReturnFunction();


  private static final long serialVersionUID = -7242229684085361882L;

  @Override
  public String getName() {
    return "__reducer_return";
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1) {
    return ReducerResult.withReturn(AviatorRuntimeJavaType.valueOf(arg1.getValue(env)));
  }
}
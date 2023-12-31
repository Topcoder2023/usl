package com.googlecode.aviator.runtime.function.internal;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * Internal reducer-break function for 'for-loop' structure.
 *
 * @since 5.0.0
 * @author dennis(killme2008@gmail.com)
 *
 */
public class ReducerBreakFunction extends AbstractFunction {

  private ReducerBreakFunction() {

  }

  public static final ReducerBreakFunction INSTANCE = new ReducerBreakFunction();

  private static final long serialVersionUID = -542526309712482544L;

  @Override
  public String getName() {
    return "__reducer_break";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env) {
    return ReducerResult.withBreak(AviatorNil.NIL);
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {
    return ReducerResult.withBreak(AviatorNil.NIL);
  }
}

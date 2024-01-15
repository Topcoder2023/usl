package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.type.AviatorNil;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

import java.util.Map;

/**
 * Internal reducer-break function for 'for-loop' structure.
 *
 * @since 5.0.0
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
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

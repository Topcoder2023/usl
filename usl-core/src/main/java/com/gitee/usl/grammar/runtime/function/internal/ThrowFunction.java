package com.gitee.usl.grammar.runtime.function.internal;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.googlecode.aviator.exception.StandardError;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.utils.Reflector;

/**
 * __throw(e) to throw an exception.
 *
 * @author boyan(boyan@antfin.com)
 *
 */
@SystemFunction
public class ThrowFunction extends AbstractFunction {

  private static final long serialVersionUID = -8464670257920503718L;

  private ThrowFunction() {}

  public static final ThrowFunction INSTANCE = new ThrowFunction();

  @Override
  public String getName() {
    return "__throw";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {
    Object val = arg1.getValue(env);
    if (val instanceof Throwable) {
      throw Reflector.sneakyThrow((Throwable) val);
    } else {
      throw Reflector.sneakyThrow(new StandardError(val == null ? null : val.toString()));
    }
  }

}

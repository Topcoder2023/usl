package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

import java.util.Map;

/**
 *
 * with_meta(obj, key, value) function to add metadata key/value to obj, return the obj.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
public class WithMetaFunction extends AbstractFunction {
  private WithMetaFunction() {

  }

  public static final WithMetaFunction INSTANCE = new WithMetaFunction();

  private static final long serialVersionUID = 7765397596826385646L;

  @Override
  public String getName() {
    return "with_meta";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2, final AviatorObject arg3) {
    return arg1.withMeta(arg2.getValue(env), arg3.getValue(env));
  }

}

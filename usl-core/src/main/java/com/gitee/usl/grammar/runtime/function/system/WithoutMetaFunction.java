package com.gitee.usl.grammar.runtime.function.system;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

/**
 *
 * without_meta(obj, key) function to remove metadata by key from obj, return the obj.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
public class WithoutMetaFunction extends AbstractFunction {

  private WithoutMetaFunction() {

  }

  public static final WithoutMetaFunction INSTANCE = new WithoutMetaFunction();

  private static final long serialVersionUID = 7765397596826385646L;

  @Override
  public String getName() {
    return "without_meta";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2) {
    return arg1.withoutMeta(arg2.getValue(env));
  }

}

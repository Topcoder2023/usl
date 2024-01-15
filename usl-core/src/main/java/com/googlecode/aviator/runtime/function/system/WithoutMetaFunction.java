package com.googlecode.aviator.runtime.function.system;

import java.util.Map;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.type.USLObject;

/**
 *
 * without_meta(obj, key) function to remove metadata by key from obj, return the obj.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
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
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2) {
    return arg1.withoutMeta(arg2.getValue(env));
  }

}

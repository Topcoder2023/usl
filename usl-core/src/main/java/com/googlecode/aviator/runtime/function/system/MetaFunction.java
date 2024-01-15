package com.googlecode.aviator.runtime.function.system;

import java.util.Map;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

/**
 *
 * meta(obj, [key]) function to return the meatadata of obj [with the key], returns null if ithere
 * is no metadata.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class MetaFunction extends AbstractFunction {

  private MetaFunction() {

  }

  public static final MetaFunction INSTANCE = new MetaFunction();

  private static final long serialVersionUID = 7765397596826385646L;

  @Override
  public String getName() {
    return "meta";
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1) {
    final Map<String, Object> m = arg1.getMetadata();
    return AviatorRuntimeJavaType.valueOf(m.isEmpty() ? null : m);
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2) {
    return AviatorRuntimeJavaType.valueOf(arg1.meta(arg2.getValue(env)));
  }

}

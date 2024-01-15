package com.gitee.usl.grammar.runtime.function.system;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.runtime.type.AviatorBoolean;
import com.gitee.usl.grammar.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.runtime.type.AviatorType;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

/**
 * is_def(x) returns true when variable x is defined in current scope or parent scopes.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
public class IsDefFunction extends AbstractFunction {


  private static final long serialVersionUID = 8641929538658275527L;

  @Override
  public String getName() {
    return "is_def";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {
    if (arg1.getAviatorType() != AviatorType.JavaType) {
      throw new IllegalArgumentException(
          "Invalid argument type for is_def: " + arg1.getAviatorType());
    }
    final String varName = ((AviatorJavaType) arg1).getName();
    return AviatorBoolean.valueOf(
        env.containsKey(varName) || RuntimeUtils.getInstance(env).existsSystemFunction(varName));
  }

}

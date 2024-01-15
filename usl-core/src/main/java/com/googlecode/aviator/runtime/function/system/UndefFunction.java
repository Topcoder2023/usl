package com.googlecode.aviator.runtime.function.system;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorType;
import com.googlecode.aviator.utils.Env;

import java.util.Map;

/**
 * undef(x) to forgot a variable that is defined in current scope.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class UndefFunction extends AbstractFunction {


  private static final long serialVersionUID = -1301889134837125717L;

  @Override
  public String getName() {
    return "undef";
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1) {
    if (arg1.getAviatorType() != AviatorType.JavaType) {
      throw new IllegalArgumentException(
          "Invalid argument type for undef: " + arg1.getAviatorType());
    }
    return FunctionUtils.wrapReturn(((Env) env).forgot(((AviatorJavaType) arg1).getName()));
  }

}

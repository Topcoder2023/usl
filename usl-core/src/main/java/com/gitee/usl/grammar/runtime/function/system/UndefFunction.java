package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.function.FunctionUtils;
import com.gitee.usl.grammar.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.runtime.type.AviatorType;
import com.gitee.usl.grammar.utils.Env;

import java.util.Map;

/**
 * undef(x) to forgot a variable that is defined in current scope.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
@SystemFunction
public class UndefFunction extends AbstractFunction {


  private static final long serialVersionUID = -1301889134837125717L;

  @Override
  public String getName() {
    return "undef";
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {
    if (arg1.getAviatorType() != AviatorType.JavaType) {
      throw new IllegalArgumentException(
          "Invalid argument type for undef: " + arg1.getAviatorType());
    }
    return FunctionUtils.wrapReturn(((Env) env).forgot(((AviatorJavaType) arg1).getName()));
  }

}

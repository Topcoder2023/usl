package com.gitee.usl.grammar.runtime.function.system;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.AbstractVariadicFunction;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

import java.util.Map;

/**
 * Constant function to return the argument itself.
 *
 * @author dennis(killme2008@gmail.com)
 * @since 4.2.5
 *
 */
@SystemFunction
public class ConstantFunction extends AbstractVariadicFunction {


  private static final long serialVersionUID = -2077433391081175967L;
  private final String name;
  private final AviatorObject result;



  public ConstantFunction(final String name, final AviatorObject result) {
    super();
    this.name = name;
    this.result = result;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public AviatorObject variadicCall(final Map<String, Object> env, final AviatorObject... args) {
    return this.result;
  }

}

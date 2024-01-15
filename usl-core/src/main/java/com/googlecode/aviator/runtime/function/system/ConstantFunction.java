package com.googlecode.aviator.runtime.function.system;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.gitee.usl.grammar.type.USLObject;

import java.util.Map;

/**
 * Constant function to return the argument itself.
 *
 * @author dennis(killme2008@gmail.com)
 * @since 4.2.5
 *
 */
public class ConstantFunction extends AbstractVariadicFunction {


  private static final long serialVersionUID = -2077433391081175967L;
  private final String name;
  private final USLObject result;



  public ConstantFunction(final String name, final USLObject result) {
    super();
    this.name = name;
    this.result = result;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public USLObject variadicCall(final Map<String, Object> env, final USLObject... args) {
    return this.result;
  }

}

package com.googlecode.aviator.runtime.function.internal;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.gitee.usl.grammar.type.USLFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.utils.Env;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * __catch_handler(fun, exception) to create a {@link CatchHandler}.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class CatchHandlerFunction extends AbstractVariadicFunction {

  private CatchHandlerFunction() {

  }

  public static final CatchHandlerFunction INSTANCE = new CatchHandlerFunction();

  /**
   *
   */
  private static final long serialVersionUID = 7314510329619948965L;

  @Override
  public String getName() {
    return "__catch_handler";
  }

  @Override
  public USLObject variadicCall(final Map<String, Object> env, final USLObject... args) {
    assert (args.length > 0);
    List<String> exceptionClasses = new ArrayList<String>(args.length - 1);
    for (int i = 1; i < args.length; i++) {
      exceptionClasses.add(((AviatorJavaType) args[i]).getName());
    }

    return new CatchHandler((Env) env, (USLFunction) args[0], exceptionClasses);
  }
}

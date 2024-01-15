package com.googlecode.aviator.runtime.function;

import java.util.List;
import java.util.Map;
import com.gitee.usl.grammar.asm.BS;
import com.gitee.usl.grammar.asm.Script;
import com.googlecode.aviator.runtime.FunctionParam;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.AviatorType;
import com.googlecode.aviator.utils.Env;

/**
 * All lamabda function base class
 *
 * @author dennis
 *
 */
public final class LambdaFunction extends AbstractVariadicFunction {


  private static final long serialVersionUID = -4388339706945053452L;

  // the arguments.
  protected List<FunctionParam> params;

  // the compiled lambda body expression
  protected BS expression;

  // closure context
  protected Env context;

  // whether to inherit parent env
  private boolean inheritEnv = false;

  private final boolean isVariadic;

  private final String name;

  // Whether to be installed in dispatch function.
  private boolean installed;

  public boolean isInstalled() {
    return this.installed;
  }

  public void setInstalled(final boolean installed) {
    this.installed = installed;
  }

  public boolean isInheritEnv() {
    return this.inheritEnv;
  }

  public Env getContext() {
    return this.context;
  }

  public void setContext(final Env context) {
    this.context = context;
  }

  public void resetContext() {
    if (this.inheritEnv) {
      // gc friendly
      this.context = null;
    }
  }

  public LambdaFunction(final String name, final List<FunctionParam> params,
                        final Script expression, final Env context) {
    super();
    this.name = name;
    this.params = params;
    this.context = context;
    this.expression = (BS) expression;
    if (!this.params.isEmpty()) {
      this.isVariadic = this.params.get(this.params.size() - 1).isVariadic();
    } else {
      this.isVariadic = false;
    }
  }

  public BS getExpression() {
    return this.expression;
  }

  public void setInheritEnv(final boolean inheritEnv) {
    this.inheritEnv = inheritEnv;
  }


  @Override
  public String getName() {
    return this.name;
  }


  public int getArity() {
    return this.params.size();
  }

  public boolean isVariadic() {
    return this.isVariadic;
  }

  @Override
  public USLObject call(final Map<String, Object> env) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true);
      }
      return AviatorRuntimeJavaType.valueOf(this.expression.defaultImpl(newEnv(env)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1);
      }
      return AviatorRuntimeJavaType.valueOf(this.expression.defaultImpl(newEnv(env, arg1)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1, arg2);
      }
      return AviatorRuntimeJavaType
          .valueOf(this.expression.defaultImpl(newEnv(env, arg1, arg2)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1, arg2, arg3);
      }
      return AviatorRuntimeJavaType
          .valueOf(this.expression.defaultImpl(newEnv(env, arg1, arg2, arg3)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1, arg2, arg3, arg4);
      }
      return AviatorRuntimeJavaType
          .valueOf(this.expression.defaultImpl(newEnv(env, arg1, arg2, arg3, arg4)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }

  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1, arg2, arg3, arg4, arg5);
      }
      return AviatorRuntimeJavaType
          .valueOf(this.expression.defaultImpl(newEnv(env, arg1, arg2, arg3, arg4, arg5)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1, arg2, arg3, arg4, arg5, arg6);
      }
      return AviatorRuntimeJavaType.valueOf(
          this.expression.defaultImpl(newEnv(env, arg1, arg2, arg3, arg4, arg5, arg6)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7) {
    try {
      if (this.isVariadic && !this.installed) {
        return variadicCall(env, true, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
      }
      return AviatorRuntimeJavaType.valueOf(
          this.expression.defaultImpl(newEnv(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7)));
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }


  protected Map<String, Object> newEnv(final Map<String, Object> parentEnv,
      final USLObject... args) {
    Env env = null;
    if (!this.inheritEnv) {
      final Env contextEnv = new Env(parentEnv, this.context);
      contextEnv.configure(this.context.getInstance(), this.expression);
      env = new Env(contextEnv);
      env.configure(this.context.getInstance(), this.expression);
    } else {
      // assert (parentEnv == this.context);
      env = (Env) parentEnv;
    }

    if (args.length != this.params.size()) {
      throw new IllegalArgumentException("Wrong number of args(" + args.length + ") passed to "
          + getName() + "(" + this.params.size() + ")");
    }

    for (int i = 0; i < this.params.size(); i++) {
      FunctionParam param = this.params.get(i);
      final USLObject arg = args[i];
      Object value = arg.getValue(parentEnv);
      if (value == null && arg.getAviatorType() == AviatorType.JavaType
          && !parentEnv.containsKey(((AviatorJavaType) arg).getName())) {
        value = RuntimeUtils.getInstance(parentEnv).getFunction(((AviatorJavaType) arg).getName());
      }
      env.override(param.getName(), value);
    }

    return env;
  }

  public USLObject variadicCall(final Map<String, Object> env, final boolean processArgs,
                                USLObject... args) {
    if (processArgs) {
      args = DispatchFunction.processVariadicArgs(env, args.length, this, args);
    }

    return AviatorRuntimeJavaType.valueOf(this.expression.defaultImpl(newEnv(env, args)));
  }

  @Override
  public USLObject variadicCall(final Map<String, Object> env, final USLObject... args) {
    try {
      return this.variadicCall(env, false, args);
    } finally {
      if (this.inheritEnv) {
        this.context = null;
      }
    }
  }

}

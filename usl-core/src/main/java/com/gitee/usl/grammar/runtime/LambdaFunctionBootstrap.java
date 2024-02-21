package com.gitee.usl.grammar.runtime;

import com.gitee.usl.grammar.runtime.function.LambdaFunction;
import com.gitee.usl.grammar.script.BS;
import com.gitee.usl.grammar.script.Script;
import com.gitee.usl.grammar.utils.Env;

import java.io.Serializable;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.List;

/**
 * A lambda function creator.
 *
 * @author dennis
 *
 */
public class LambdaFunctionBootstrap implements Comparable<LambdaFunctionBootstrap>, Serializable {
  private static final long serialVersionUID = -8884911908304713609L;
  // the generated lambda class name
  private final String name;
  // The compiled lambda body expression
  private final BS expression;
  // The method handle to create lambda instance.
  // private final MethodHandle constructor;
  // The arguments list.
  private final List<FunctionParam> params;
  private final boolean inheritEnv;

  private transient ThreadLocal<Reference<LambdaFunction>> fnLocal = new ThreadLocal<>();


  @Override
  public int compareTo(final LambdaFunctionBootstrap o) {
    return this.name.compareTo(o.name);
  }

  public String getName() {
    return this.name;
  }

  public LambdaFunctionBootstrap(final String name, final Script expression,
      final List<FunctionParam> arguments, final boolean inheritEnv) {
    super();
    this.name = name;
    this.expression = (BS) expression;
    // this.constructor = constructor;
    this.params = arguments;
    this.inheritEnv = inheritEnv;
  }


  public Script getExpression() {
    return this.expression;
  }

  /**
   * Create a lambda function.
   *
   * @param env
   * @return
   */
  public LambdaFunction newInstance(final Env env) {
    Reference<LambdaFunction> ref = null;
    if (this.fnLocal == null) {
      this.fnLocal = new ThreadLocal<Reference<LambdaFunction>>();
    }
    if (this.inheritEnv && (ref = this.fnLocal.get()) != null) {
      LambdaFunction fn = ref.get();
      if (fn != null) {
        fn.setContext(env);
        return fn;
      } else {
        this.fnLocal.remove();
      }
    }

    LambdaFunction fn = new LambdaFunction(this.name, this.params, this.expression, env);
    fn.setInheritEnv(this.inheritEnv);
    if (this.inheritEnv) {
      this.fnLocal.set(new SoftReference<>(fn));
    }
    return fn;
  }
}

package com.googlecode.aviator.runtime.function;

import com.googlecode.aviator.runtime.RuntimeUtils;
import com.gitee.usl.grammar.type.USLFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.utils.Env;

import java.util.Map;

/**
 * Trace eval function.
 *
 * @author dennis
 *
 */
public class TraceFunction implements USLFunction {

  @Override
  public USLObject call() throws Exception {
    return this.call(Env.EMPTY_ENV);
  }

  @Override
  public void run() {
    this.call(Env.EMPTY_ENV);
  }

  private final USLFunction rawFunc;

  private TraceFunction(final USLFunction rawFunc) {
    super();
    this.rawFunc = rawFunc;
  }

  public static USLFunction wrapTrace(final USLFunction func) {
    return new TraceFunction(func);
  }

  private void traceResult(final Map<String, Object> env, final Object result) {
    RuntimeUtils.printlnTrace(env, "Result : " + result);
  }

  private void traceArgs(final Map<String, Object> env, final Object... args) {
    StringBuilder sb = new StringBuilder();
    boolean wasFirst = true;
    for (Object arg : args) {
      if (wasFirst) {
        wasFirst = false;
      } else {
        sb.append(",");
      }
      if (arg instanceof String) {
        sb.append(arg.toString());
      } else {
        sb.append(((USLObject) arg).desc(env));
      }
    }
    RuntimeUtils.printlnTrace(env, "Func   : " + getName() + "(" + sb.toString() + ")");
  }

  @Override
  public String getName() {
    return this.rawFunc.getName();
  }

  @Override
  public USLObject call(final Map<String, Object> env) {
    traceArgs(env);
    USLObject ret = this.rawFunc.call(env);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1) {
    traceArgs(env, arg1);
    USLObject ret = this.rawFunc.call(env, arg1);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2) {
    traceArgs(env, arg1, arg2);
    USLObject ret = this.rawFunc.call(env, arg1, arg2);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3) {
    traceArgs(env, arg1, arg2, arg3);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4) {
    traceArgs(env, arg1, arg2, arg3, arg4);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    USLObject ret =
        this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    USLObject ret =
        this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    USLObject ret =
        this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12,
        arg13);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15, arg16);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15, arg16);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15, arg16, arg17);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17, final USLObject arg18) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15, arg16, arg17, arg18);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17, final USLObject arg18, final USLObject arg19) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15, arg16, arg17, arg18, arg19);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17, final USLObject arg18, final USLObject arg19,
                        final USLObject arg20) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15, arg16, arg17, arg18, arg19, arg20);
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
    traceResult(env, ret);
    return ret;
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17, final USLObject arg18, final USLObject arg19,
                        final USLObject arg20, final USLObject... args) {
    traceArgs(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13,
        arg14, arg15, arg16, arg17, arg18, arg19, arg20, "...");
    USLObject ret = this.rawFunc.call(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9,
        arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, args);
    traceResult(env, ret);
    return ret;
  }
}

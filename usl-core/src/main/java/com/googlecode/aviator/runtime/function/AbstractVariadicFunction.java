package com.googlecode.aviator.runtime.function;

import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.gitee.usl.grammar.type.USLFunction;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.AviatorType;
import com.googlecode.aviator.utils.Env;

import java.util.Map;


/**
 * Abstract function to implement variadic arguments function.
 *
 * @author dennis
 * @since 3.0.0
 * @Date 2016-12-09
 *
 */
public abstract class AbstractVariadicFunction extends USLObject implements USLFunction {


  private static final long serialVersionUID = -5939898720859638046L;

  @Override
  public USLObject call() throws Exception {
    return this.call(Env.EMPTY_ENV);
  }

  @Override
  public void run() {
    this.call(Env.EMPTY_ENV);
  }


  @Override
  public AviatorType getAviatorType() {
    return AviatorType.Lambda;
  }

  @Override
  public String desc(final Map<String, Object> env) {
    return "<" + getAviatorType() + ", " + getName() + ">";
  }


  @Override
  public Object getValue(final Map<String, Object> env) {
    return this;
  }

  @Override
  public int innerCompare(final USLObject other, final Map<String, Object> env) {
    throw new CompareNotSupportedException("Lambda function can't be compared.");
  }

  @Override
  public USLObject call(final Map<String, Object> env) {
    return variadicCall(env);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1) {
    return variadicCall(env, arg1);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2) {
    return variadicCall(env, arg1, arg2);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3) {
    return variadicCall(env, arg1, arg2, arg3);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4) {
    return variadicCall(env, arg1, arg2, arg3, arg4);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14, arg15);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14, arg15, arg16);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14, arg15, arg16, arg17);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17, final USLObject arg18) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14, arg15, arg16, arg17, arg18);
  }


  @Override
  public USLObject call(final Map<String, Object> env, final USLObject arg1,
                        final USLObject arg2, final USLObject arg3, final USLObject arg4,
                        final USLObject arg5, final USLObject arg6, final USLObject arg7,
                        final USLObject arg8, final USLObject arg9, final USLObject arg10,
                        final USLObject arg11, final USLObject arg12, final USLObject arg13,
                        final USLObject arg14, final USLObject arg15, final USLObject arg16,
                        final USLObject arg17, final USLObject arg18, final USLObject arg19) {
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19);
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
    return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
        arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
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
    if (args == null || args.length == 0) {
      return variadicCall(env, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
          arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20);
    } else {
      USLObject[] allArgs = new USLObject[20 + args.length];
      allArgs[0] = arg1;
      allArgs[1] = arg2;
      allArgs[2] = arg3;
      allArgs[3] = arg4;
      allArgs[4] = arg5;
      allArgs[5] = arg6;
      allArgs[6] = arg7;
      allArgs[7] = arg8;
      allArgs[8] = arg9;
      allArgs[9] = arg10;
      allArgs[10] = arg11;
      allArgs[11] = arg12;
      allArgs[12] = arg13;
      allArgs[13] = arg14;
      allArgs[14] = arg15;
      allArgs[15] = arg16;
      allArgs[16] = arg17;
      allArgs[17] = arg18;
      allArgs[18] = arg19;
      allArgs[19] = arg20;
      System.arraycopy(args, 0, allArgs, 20, args.length);
      return variadicCall(env, allArgs);
    }
  }


  /**
   * Call with variadic arguments.The subclass must implement this method.
   *
   * @since 3.0.0
   * @param env
   * @param args
   * @return
   */
  public abstract USLObject variadicCall(Map<String, Object> env, USLObject... args);

}

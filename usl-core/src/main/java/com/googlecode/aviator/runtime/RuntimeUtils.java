package com.googlecode.aviator.runtime;

import com.gitee.usl.grammar.ScriptEngine;
import com.googlecode.aviator.Options;
import com.googlecode.aviator.runtime.function.LambdaFunction;
import com.googlecode.aviator.runtime.function.internal.UnpackingArgsFunction;
import com.gitee.usl.grammar.type.USLFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.runtime.type.Sequence;
import com.googlecode.aviator.runtime.type.seq.*;
import com.googlecode.aviator.utils.Env;

import java.math.MathContext;
import java.util.Map;

/**
 * Runtime utils
 *
 * @author dennis
 *
 */
public final class RuntimeUtils {

  private RuntimeUtils() {

  }

  /**
   * Get the current evaluator instance,returns the global instance if not found.
   *
   * @return
   */
  public static final ScriptEngine getInstance(final Map<String, Object> env) {
    if (env instanceof Env) {
      return ((Env) env).getInstance();
    }
    return new ScriptEngine();

  }

  /**
   * Wrap the function to unpacking-arguments function.
   *
   * @param fn
   * @return
   */
  public static final USLFunction unpackArgsFunction(final USLFunction fn) {
    if (fn instanceof UnpackingArgsFunction) {
      return fn;
    }
    return new UnpackingArgsFunction(fn);
  }

  public static void resetLambdaContext(USLFunction fn) {
    if (fn != null && fn instanceof LambdaFunction) {
      ((LambdaFunction) fn).resetContext();
    }
  }

  /**
   * Cast an object into sequence if possible, otherwise throw an exception.
   *
   * @param o
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static Sequence seq(final Object o, final Map<String, Object> env) {
    Sequence seq = null;

    if (o == null) {
      return EmptySequence.INSTANCE;
    } else if (o instanceof Sequence) {
      seq = (Sequence) o;
    } else if (o instanceof CharSequence) {
      seq = new CharSeqSequence((CharSequence) o);
    } else if (o instanceof Iterable) {
      seq = new IterableSequence((Iterable) o);
    } else if (o.getClass().isArray()) {
      seq = new ArraySequence(o);
    } else if (o instanceof Map) {
      seq = new MapSequence((Map) o);
    } else {
      throw new IllegalArgumentException(o + " is not a sequence");
    }

    if (env instanceof Env) {
      int maxLoopCount =
          RuntimeUtils.getInstance(env).getOptionValue(Options.MAX_LOOP_COUNT).number;
      if (maxLoopCount > 0) {
        return new LimitedSequence<>(seq, maxLoopCount);
      }
    }

    return seq;
  }

  /**
   * Ensure the object is not null, cast null into AviatorNil.
   *
   * @param object
   * @return
   */
  public static final USLObject assertNotNull(final USLObject object) {
    if (object != null) {
      return object;
    }
    return AviatorNil.NIL;
  }

  public static final MathContext getMathContext(final Map<String, Object> env) {
    return getInstance(env).getOptionValue(Options.MATH_CONTEXT).mathContext;
  }


  public static final void printlnTrace(final Map<String, Object> env, final String msg) {

  }

  public static final boolean isTracedEval(final Map<String, Object> env) {
    return getInstance(env).getOptionValue(Options.TRACE_EVAL).bool;
  }

  public static USLFunction getFunction(final Object object, final Map<String, Object> env) {
    if (object instanceof USLFunction) {
      return (USLFunction) object;
    } else if (object instanceof USLObject) {
      Object value = ((USLObject) object).getValue(env);
      if (value instanceof USLFunction) {
        return (USLFunction) value;
      }
    }
    throw new ClassCastException("Could not cast object " + object + " into a aviator function.");
  }

  public static USLFunction getFunction(final Map<String, Object> env, final String name) {
    return getInstance(env).getFunction(name);
  }

  public static void printStackTrace(final Map<String, Object> env, final Exception e) {
    if (isTracedEval(env)) {
      e.printStackTrace();
    }
  }
}

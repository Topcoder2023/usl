package com.googlecode.aviator;
/**
 *
 * Called when function not found, return the invocation result.
 *
 * @author dennis(killme2008@gmail.com)
 * @since 4.2.5
 *
 */

import com.gitee.usl.grammar.ScriptEngine;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.io.Serializable;
import java.util.Map;

/**
 * Function not found hook interface. The
 * {@link FunctionMissing#onFunctionMissing(String, Map, AviatorObject...)} method will be called
 * when function not found, return the invocation result.
 *
 * @see ScriptEngine#setFunctionMissing(FunctionMissing)
 * @see AviatorEvaluator#setFunctionMissing(FunctionMissing)
 * @author dennis zhuang(killme2008@gmail.com)
 * @since 4.2.5
 *
 */
public interface FunctionMissing extends Serializable {
  /**
   * Called when function not found, return the invocation result.
   *
   * @param name function name
   * @param env invocation env
   * @param args invocation arguments.
   * @return The invocation result.
   */
  AviatorObject onFunctionMissing(String name, Map<String, Object> env, AviatorObject... args);
}

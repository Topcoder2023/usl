package com.googlecode.aviator;

import com.googlecode.aviator.runtime.type.AviatorFunction;

/**
 * Function loader to load function when function not found.
 *
 * @author dennis
 *
 */
public interface FunctionLoader {

  /**
   * Invoked when function not found, <strong>The implementation must returns null when function not
   * found.</strong>
   *
   * @param name function name
   */
  public AviatorFunction onFunctionNotFound(String name);
}

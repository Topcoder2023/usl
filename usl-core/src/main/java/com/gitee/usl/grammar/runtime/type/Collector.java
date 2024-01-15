package com.gitee.usl.grammar.runtime.type;

/**
 * Collector to collect elements.
 *
 * @author dennis(killme2008@gmail.com)
 *
 * @param <T>
 */
public interface Collector {
  void add(Object e);

  Object getRawContainer();
}

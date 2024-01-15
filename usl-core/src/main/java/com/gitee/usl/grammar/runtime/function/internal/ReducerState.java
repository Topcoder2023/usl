package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;

/**
 *
 * @author dennis(killme2008@gmail.com)
 * @since 5.0.0
 */
@SystemFunction
public enum ReducerState {
  Empty, Cont, Break, Return
}

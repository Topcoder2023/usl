package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemFunction;

/**
 * @author hongda.li
 */
@SystemFunction
public enum ReducerState {
    Empty, Cont, Break, Return
}

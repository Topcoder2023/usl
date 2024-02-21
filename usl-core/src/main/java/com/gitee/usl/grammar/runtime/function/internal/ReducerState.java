package com.gitee.usl.grammar.runtime.function.internal;

import com.gitee.usl.api.annotation.SystemComponent;

/**
 * @author hongda.li
 */
@SystemComponent
public enum ReducerState {
    Empty, Cont, Break, Return
}

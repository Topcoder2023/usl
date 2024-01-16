package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
public interface _Function {

    String name();

    _Object execute(Env env, _Object... arguments);

}

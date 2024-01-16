package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.utils.Env;

/**
 * @author hongda.li
 */
public interface Function {

    String name();

    AviatorObject execute(Env env, AviatorObject... arguments);

}

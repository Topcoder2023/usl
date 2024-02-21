package com.gitee.usl.api;

import com.gitee.usl.grammar.lexer.token.Variable;

/**
 * 变量初始化接口
 *
 * @author hongda.li
 */
public interface VariableDefinable {

    /**
     * 执行变量初始化
     *
     * @param variable 脚本中存在的变量
     * @return 变量的初始值
     */
    Object define(Variable variable);

}

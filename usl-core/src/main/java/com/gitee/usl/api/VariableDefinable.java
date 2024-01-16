package com.gitee.usl.api;

import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.grammar.lexer.token.Variable;

/**
 * @author hongda.li
 */
@Description("变量初始化接口")
public interface VariableDefinable {

    @Description("执行变量初始化")
    Object define(@Description("脚本中存在的变量") Variable variable);

}

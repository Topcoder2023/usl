package com.gitee.usl.infra.structure;

import com.googlecode.aviator.lexer.token.Variable;

import java.util.Map;

/**
 * @author hongda.li
 */
public class FunctionVariable extends Variable {
    private static final String NAME = "function";
    private static final FunctionVariable VARIABLE = new FunctionVariable();

    private FunctionVariable() {
        super(NAME, 0, -1);
    }

    public static FunctionVariable getInstance() {
        return VARIABLE;
    }

    @Override
    public Object getValue(Map<String, Object> env) {
        return this;
    }
}

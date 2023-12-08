package com.gitee.usl.infra.structure;

import com.googlecode.aviator.lexer.token.Variable;

import java.util.Map;

/**
 * @author hongda.li
 */
public class NullVariable extends Variable {
    private static final String NAME = "null";
    private static final NullVariable VARIABLE = new NullVariable();

    private NullVariable() {
        super(NAME, 0, -1);
    }

    public static NullVariable getInstance() {
        return VARIABLE;
    }

    @Override
    public Object getValue(Map<String, Object> env) {
        return this;
    }
}

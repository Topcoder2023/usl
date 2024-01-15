package com.gitee.usl.infra.structure;

import com.gitee.usl.grammar.lexer.token.Variable;

import java.util.Map;

/**
 * @author hongda.li
 */
public class AwaitVariable extends Variable {
    private static final String NAME = "await";
    private static final AwaitVariable VARIABLE = new AwaitVariable();

    private AwaitVariable() {
        super(NAME, 0, -1);
    }

    public static AwaitVariable getInstance() {
        return VARIABLE;
    }

    @Override
    public Object getValue(Map<String, Object> env) {
        return this;
    }
}

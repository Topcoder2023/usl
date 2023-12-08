package com.gitee.usl.infra.structure;

import com.googlecode.aviator.lexer.token.Variable;

import java.util.Map;

/**
 * @author hongda.li
 */
public class VarVariable extends Variable {
    private static final String NAME = "var";
    private static final VarVariable VARIABLE = new VarVariable();

    private VarVariable() {
        super(NAME, 0, -1);
    }

    public static VarVariable getInstance() {
        return VARIABLE;
    }

    @Override
    public Object getValue(Map<String, Object> env) {
        return this;
    }
}

package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;

/**
 * @author hongda.li
 */
public record VisitLabelIR(Label label) implements IR {
    @Override
    public void eval(final InterpretContext context) {
        throw new UnsupportedOperationException();
    }

}

package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;

/**
 * @author hongda.li
 */
public record VisitLabelIR(Label label) implements IR {
    @Override
    public void eval(final InterpretContext context) {
        throw new UnsupportedOperationException();
    }

}

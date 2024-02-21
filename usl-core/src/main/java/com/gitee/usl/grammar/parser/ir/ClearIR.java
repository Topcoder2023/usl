package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;

/**
 * @author hongda.li
 */
public class ClearIR implements IR {

    private ClearIR() {
    }

    public static final ClearIR INSTANCE = new ClearIR();

    @Override
    public void eval(final InterpretContext context) {
        context.clearStack();
        context.dispatch();
    }

}

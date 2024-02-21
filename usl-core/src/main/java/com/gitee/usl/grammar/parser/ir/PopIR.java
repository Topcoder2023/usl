package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;

public class PopIR implements IR {

    public static PopIR INSTANCE = new PopIR();

    private PopIR() {
    }

    @Override
    public void eval(final InterpretContext context) {
        context.pop();
        context.dispatch();
    }

}

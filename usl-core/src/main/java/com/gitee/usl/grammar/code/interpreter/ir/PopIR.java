package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;

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

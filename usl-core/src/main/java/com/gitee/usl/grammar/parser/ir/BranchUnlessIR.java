package com.gitee.usl.grammar.parser.ir;

import com.gitee.usl.grammar.parser.IR;
import com.gitee.usl.grammar.parser.InterpretContext;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BranchUnlessIR implements IR, JumpIR {

    private int pc;

    private final Label label;

    public BranchUnlessIR(final Label label) {
        this.label = label;
    }

    @Override
    public void setPc(final int pc) {
        this.pc = pc;
    }

    @Override
    public Label getLabel() {
        return this.label;
    }

    @Override
    public void eval(final InterpretContext context) {
        AviatorObject top = context.peek();
        if (!top.booleanValue(context.getEnv())) {
            context.jumpTo(this.pc);
            context.dispatch(false);
        } else {
            context.dispatch();
        }
    }

}

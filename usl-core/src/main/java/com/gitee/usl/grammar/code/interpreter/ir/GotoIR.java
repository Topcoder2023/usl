package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;
import lombok.Getter;
import lombok.ToString;

/**
 * @author hongda.li
 */
@Getter
@ToString
public class GotoIR implements IR, JumpIR {

    private int pc;

    private final Label label;

    public GotoIR(final Label label) {
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
        context.jumpTo(this.pc);
        context.dispatch(false);
    }

}

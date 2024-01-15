package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;
import com.gitee.usl.grammar.runtime.type.AviatorObject;

public class BranchUnlessIR implements IR, JumpIR {
  private static final long serialVersionUID = 262499564579405793L;
  private int pc;
  private final Label label;
  private final SourceInfo sourceInfo;


  public BranchUnlessIR(final Label label, final SourceInfo sourceInfo) {
    super();
    this.label = label;
    this.sourceInfo = sourceInfo;
  }

  public int getPc() {
    return this.pc;
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

  @Override
  public String toString() {
    return "branch_unless " + this.pc + "  [" + this.label + "]      " + this.sourceInfo;
  }
}

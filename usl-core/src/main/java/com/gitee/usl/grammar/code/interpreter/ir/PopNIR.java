package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;

public class PopNIR implements IR {
  private static final long serialVersionUID = -7275602629270711681L;
  private final int times;


  public PopNIR(final int times) {
    this.times = times;
  }

  @Override
  public void eval(final InterpretContext context) {
    int i = this.times;
    while (i-- > 0) {
      context.pop();
    }
    context.dispatch();
  }

  @Override
  public String toString() {
    return "pop " + this.times;
  }

}

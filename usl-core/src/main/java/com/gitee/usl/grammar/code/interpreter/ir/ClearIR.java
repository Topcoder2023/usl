package com.gitee.usl.grammar.code.interpreter.ir;

import com.gitee.usl.grammar.code.interpreter.IR;
import com.gitee.usl.grammar.code.interpreter.InterpretContext;

public class ClearIR implements IR {

  private static final long serialVersionUID = -486328244006736142L;

  private ClearIR() {};

  public static final ClearIR INSTANCE = new ClearIR();

  @Override
  public void eval(final InterpretContext context) {
    context.clearStack();
    context.dispatch();
  }

  @Override
  public String toString() {
    return "clear";
  }

}

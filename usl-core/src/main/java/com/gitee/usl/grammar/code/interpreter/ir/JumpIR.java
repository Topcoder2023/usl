package com.gitee.usl.grammar.code.interpreter.ir;

public interface JumpIR {
  void setPc(int pc);

  Label getLabel();
}

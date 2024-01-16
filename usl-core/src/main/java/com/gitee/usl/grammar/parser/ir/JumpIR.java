package com.gitee.usl.grammar.parser.ir;

/**
 * @author hongda.li
 */
public interface JumpIR {

    void setPc(int pc);

    Label getLabel();

}

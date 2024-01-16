package com.gitee.usl.grammar.runtime.type.string;

import com.gitee.usl.grammar.script.Script;

import java.util.Map;

/**
 * A string segment that generated by an expression execution.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class ExpressionSegment implements StringSegment {
  Script exp;

  public ExpressionSegment(final Script exp) {
    super();
    this.exp = exp;
  }

  @Override
  public StringBuilder appendTo(final StringBuilder sb, final Map<String, Object> env) {
    return sb.append(this.exp.execute(env));
  }

  @Override
  public String toString() {
    return "ExpressionSegment [exp=" + this.exp + "]";
  }

}
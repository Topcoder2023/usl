package com.gitee.usl.grammar.runtime.type;

import java.util.Map;

/**
 * Aviator string builder for fast concatenating string.
 *
 * @author boyan(boyan@antfin.com)
 *
 */
public class AviatorStringBuilder extends AviatorString {

  private static final long serialVersionUID = 1958289382573221857L;
  private final StringBuilder sb;

  public AviatorStringBuilder(final StringBuilder sb) {
    super(null);
    this.sb = sb;
  }

  public AviatorStringBuilder(final String lexeme) {
    super(null);
    this.sb = new StringBuilder(lexeme);
  }

  @Override
  public String getLexeme(final Map<String, Object> env, final boolean warnOnCompile) {
    return this.sb.toString();
  }


  @Override
  public AviatorObject self(final Map<String, Object> env) {
    return new AviatorString(getLexeme(env));
  }

  @Override
  public AviatorObject add(final AviatorObject other, final Map<String, Object> env) {
    if (other.getAviatorType() == AviatorType.Pattern) {
      final AviatorPattern otherPatterh = (AviatorPattern) other;
      this.sb.append(otherPatterh.pattern.pattern());
    } else {
      this.sb.append(other.getValue(env));
    }
    return new AviatorStringBuilder(this.sb);
  }
}

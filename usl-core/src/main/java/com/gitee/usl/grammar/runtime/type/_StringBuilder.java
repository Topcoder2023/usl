package com.gitee.usl.grammar.runtime.type;

import java.util.Map;

/**
 * Aviator string builder for fast concatenating string.
 *
 * @author boyan(boyan@antfin.com)
 *
 */
public class _StringBuilder extends _String {

  private static final long serialVersionUID = 1958289382573221857L;
  private final StringBuilder sb;

  public _StringBuilder(final StringBuilder sb) {
    super(null);
    this.sb = sb;
  }

  public _StringBuilder(final String lexeme) {
    super(null);
    this.sb = new StringBuilder(lexeme);
  }

  @Override
  public String getLexeme(final Map<String, Object> env, final boolean warnOnCompile) {
    return this.sb.toString();
  }


  @Override
  public _Object self(final Map<String, Object> env) {
    return new _String(getLexeme(env));
  }

  @Override
  public _Object add(final _Object other, final Map<String, Object> env) {
    if (other.getAviatorType() == _Type.Pattern) {
      final _Pattern otherPatterh = (_Pattern) other;
      this.sb.append(otherPatterh.pattern.pattern());
    } else {
      this.sb.append(other.getValue(env));
    }
    return new _StringBuilder(this.sb);
  }
}

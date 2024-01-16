/**
 * Copyright (C) 2010 dennis zhuang (killme2008@gmail.com)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 **/
package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.Options;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.utils.TypeUtils;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A Aviator regular expression pattern
 *
 * @author dennis
 *
 */
public class _Pattern extends _Object {


  private static final long serialVersionUID = -6667072156431636239L;
  final Pattern pattern;


  public _Pattern(final String expression) {
    super();
    this.pattern = Pattern.compile(expression);
  }


  public _Pattern(final Pattern pattern) {
    super();
    this.pattern = pattern;
  }


  public Pattern getPattern() {
    return this.pattern;
  }


  @Override
  public _Object add(final _Object other, final Map<String, Object> env) {
    switch (other.getAviatorType()) {
      case String:
        return new _String(this.pattern.pattern() + ((_String) other).getLexeme(env));
      case JavaType:
        _JavaType javaType = (_JavaType) other;
        final Object otherValue = javaType.getValue(env);
        if (TypeUtils.isString(otherValue)) {
          return new _String(this.pattern.pattern() + otherValue.toString());
        } else {
          return super.add(other, env);
        }
      default:
        return super.add(other, env);

    }
  }


  @Override
  public _Object match(final _Object other, final Map<String, Object> env) {
    switch (other.getAviatorType()) {
      case Nil:
        return _Bool.FALSE;
      case String:
        _String aviatorString = (_String) other;
        Matcher m = this.pattern.matcher(aviatorString.getLexeme(env));
        if (m.matches()) {
          boolean captureGroups = RuntimeUtils.getInstance(env)
              .getOptionValue(Options.PUT_CAPTURING_GROUPS_INTO_ENV).bool;
          if (captureGroups && env != Collections.EMPTY_MAP) {
            int groupCount = m.groupCount();
            for (int i = 0; i <= groupCount; i++) {
              ((Env) env).override("$" + i, m.group(i));
            }
          }
          return _Bool.TRUE;
        } else {
          return _Bool.FALSE;
        }
      case JavaType:
        _JavaType javaType = (_JavaType) other;
        final Object javaValue = javaType.getValue(env);
        if (javaValue == null) {
          return _Bool.FALSE;
        }
        if (TypeUtils.isString(javaValue)) {
          return match(new _String(String.valueOf(javaValue)), env);
        } else {
          throw new ExpressionRuntimeException(desc(env) + " could not match " + other.desc(env));
        }
      default:
        throw new ExpressionRuntimeException(desc(env) + " could not match " + other.desc(env));
    }

  }


  @Override
  public int innerCompare(final _Object other, final Map<String, Object> env) {
    if (this == other) {
      return 0;
    }
    switch (other.getAviatorType()) {
      case Pattern:
        return this.pattern.pattern().compareTo(((_Pattern) other).pattern.pattern());
      case JavaType:
        if (other.getValue(env) == null) {
          return 1;
        } else {
          throw new CompareNotSupportedException(
              "Could not compare Pattern with " + other.desc(env));
        }
      case Nil:
        return 1;
      default:
        throw new CompareNotSupportedException("Could not compare Pattern with " + other.desc(env));
    }
  }


  @Override
  public _Type getAviatorType() {
    return _Type.Pattern;
  }


  @Override
  public Object getValue(final Map<String, Object> env) {
    return this.pattern;
  }

}

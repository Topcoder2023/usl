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

import java.util.Map;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.gitee.usl.grammar.utils.TypeUtils;


/**
 * Aviator boolean type
 *
 * @author dennis
 *
 */
public class _Bool extends _Object {

  /**
   *
   */
  private static final long serialVersionUID = -685795150869373183L;

  Boolean value;

  public static final _Bool TRUE = new _Bool(Boolean.TRUE);

  public static final _Bool FALSE = new _Bool(Boolean.FALSE);


  @Override
  public _Object not(final Map<String, Object> env) {
    return this.value.booleanValue() ? FALSE : TRUE;
  }


  @Override
  public final boolean booleanValue(final Map<String, Object> env) {
    return this.value.booleanValue();
  }

  public boolean getBooleanValue() {
    return this.value;
  }

  @Override
  public _Object add(final _Object other, final Map<String, Object> env) {
    switch (other.getAviatorType()) {
      case String:
        return new _String(this.value.toString() + ((_String) other).getLexeme(env));
      case JavaType:
        _JavaType javaType = (_JavaType) other;
        final Object otherJavaValue = javaType.getValue(env);
        if (TypeUtils.isString(otherJavaValue)) {
          return new _String(this.value.toString() + otherJavaValue.toString());
        } else {
          return super.add(other, env);
        }
      default:
        return super.add(other, env);
    }

  }


  @Override
  public _Type getAviatorType() {
    return _Type.Boolean;
  }


  @Override
  public final Object getValue(final Map<String, Object> env) {
    return this.value;
  }


  private _Bool(final Boolean value) {
    super();
    this.value = value;
  }


  public static _Bool valueOf(final boolean b) {
    return b ? _Bool.TRUE : _Bool.FALSE;
  }


  @Override
  public int innerCompare(final _Object other, final Map<String, Object> env) {
    switch (other.getAviatorType()) {
      case Boolean:
        _Bool otherBoolean = (_Bool) other;
        return this.value.compareTo(otherBoolean.value);
      case JavaType:
        _JavaType javaType = (_JavaType) other;
        final Object otherValue = javaType.getValue(env);
        if (otherValue == null) {
          return 1;
        }
        if (otherValue instanceof Boolean) {
          return this.value.compareTo((Boolean) otherValue);
        } else {
          throw new CompareNotSupportedException(
              "Could not compare " + desc(env) + " with " + other.desc(env));
        }
      case Nil:
        return 1;
      default:
        throw new CompareNotSupportedException(
            "Could not compare " + desc(env) + " with " + other.desc(env));
    }

  }

}

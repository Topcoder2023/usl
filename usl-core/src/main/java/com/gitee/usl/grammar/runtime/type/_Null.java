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
import com.gitee.usl.grammar.utils.TypeUtils;


/**
 * Aviator nil object
 *
 * @author dennis
 *
 */
public class _Null extends _Object {
  private static final long serialVersionUID = 5030890238879926682L;
  public static final _Null NIL = new _Null();


  private _Null() {

  }


  @Override
  public _Object add(final _Object other, final Map<String, Object> env) {
    switch (other.getAviatorType()) {
      case String:
        return new _String("null" + other.getValue(env));
      case JavaType:
        final Object otherValue = other.getValue(env);
        if (TypeUtils.isString(otherValue)) {
          return new _String("null" + otherValue);
        } else {
          return super.add(other, env);
        }
      default:
        return super.add(other, env);
    }
  }


  @Override
  public int innerCompare(final _Object other, final Map<String, Object> env) {
    switch (other.getAviatorType()) {
      case Nil:
        return 0;
      case JavaType:
        if (other.getValue(env) == null) {
          return 0;
        }
    }
    // Any object is greater than nil except nil
    return -1;
  }


  @Override
  public _Type getAviatorType() {
    return _Type.Nil;
  }


  @Override
  public Object getValue(final Map<String, Object> env) {
    return null;
  }

}

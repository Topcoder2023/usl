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


/**
 * Aviator double type
 *
 * @author dennis
 *
 */
public class _Double extends _Number {



  private static final long serialVersionUID = 829573884607069307L;


  public _Double(double d) {
    super(d);
  }


  _Double(Number number) {
    super(number);
  }


  public static _Double valueOf(double value) {
    return new _Double(value);
  }


  public static _Double valueOf(Double value) {
    return valueOf(value.doubleValue());
  }


  @Override
  public int innerCompare(Map<String, Object> env, _Number other) {
    return Double.compare(this.doubleValue, other.doubleValue());
  }


  @Override
  public _Object neg(Map<String, Object> env) {
    return new _Double(-this.doubleValue);
  }


  @Override
  public _Object innerDiv(Map<String, Object> env, _Number other) {
    return new _Double(this.doubleValue / other.doubleValue());
  }


  @Override
  public _Number innerAdd(Map<String, Object> env, _Number other) {
    return new _Double(this.doubleValue + other.doubleValue());
  }


  @Override
  public _Object innerMod(Map<String, Object> env, _Number other) {
    return new _Double(this.doubleValue % other.doubleValue());
  }


  @Override
  public _Object innerMult(Map<String, Object> env, _Number other) {
    return new _Double(this.doubleValue * other.doubleValue());
  }

  @Override
  public long longValue() {
    return (long) this.doubleValue;
  }


  @Override
  public double doubleValue() {
    return this.doubleValue;
  }

  @Override
  public Object getValue(Map<String, Object> env) {
    return this.doubleValue;
  }


  @Override
  public _Type getAviatorType() {
    return _Type.Double;
  }


  @Override
  public _Object innerSub(Map<String, Object> env, _Number other) {
    return new _Double(this.doubleValue - other.doubleValue());
  }
}

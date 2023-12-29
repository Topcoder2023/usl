/**
 * Copyright (C) 2010 dennis zhuang (killme2008@gmail.com)
 * <p>
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with this program;
 * if not, write to the Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 **/
package com.googlecode.aviator.runtime.function.seq;

import java.util.Map;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.exception.NoSuchPropertyException;
import com.googlecode.aviator.lexer.token.OperatorType;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.utils.Reflector;


/**
 * A predicate function
 *
 * @author dennis
 */
public class SeqPredicateFunction extends AbstractFunction {

  private static final long serialVersionUID = 478017115680743291L;
  private final String name;
  private final OperatorType opType;
  private final AviatorObject value;
  private final AviatorObject propertyName;

  public SeqPredicateFunction(final String name, final OperatorType opType,
      final AviatorObject value) {
    this(name, opType, value, null);
  }

  public SeqPredicateFunction(final String name, final OperatorType opType,
      final AviatorObject value, final AviatorObject propertyName) {
    this.name = name;
    this.opType = opType;
    this.value = value;
    this.propertyName = propertyName;
  }

  @Override
  public AviatorObject call(final Map<String, Object> env, AviatorObject arg1) {
    if (this.propertyName != null) {
      String propertyNameStr = this.propertyName.stringValue(env);
      Object target = arg1.getValue(env);
      try {
        Object property = Reflector.getProperty(target, propertyNameStr);
        arg1 = AviatorRuntimeJavaType.valueOf(property);
      } catch (NoSuchPropertyException e) {
        throw new IllegalArgumentException(
            "Fail to get property <" + propertyNameStr + "> from <" + arg1.desc(env) + ">", e);
      }
    }
    switch (this.opType) {
      case EQ:
        return arg1.compare(this.value, env) == 0 ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
      case NEQ:
        return arg1.compare(this.value, env) != 0 ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
      case LT:
        return arg1.compare(this.value, env) < 0 ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
      case LE:
        return arg1.compare(this.value, env) <= 0 ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
      case GE:
        return arg1.compare(this.value, env) >= 0 ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
      case GT:
        return arg1.compare(this.value, env) > 0 ? AviatorBoolean.TRUE : AviatorBoolean.FALSE;
      default:
        throw new ExpressionRuntimeException(getName() + " is not a relation operator");
    }
  }


  @Override
  public String getName() {
    return this.name;
  }

}

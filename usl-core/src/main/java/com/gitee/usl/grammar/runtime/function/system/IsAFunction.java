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
package com.gitee.usl.grammar.runtime.function.system;

import java.util.Map;

import com.gitee.usl.api.annotation.SystemFunction;
import com.gitee.usl.grammar.runtime.function.AbstractFunction;
import com.gitee.usl.grammar.runtime.type.AviatorBoolean;
import com.gitee.usl.grammar.runtime.type.AviatorJavaType;
import com.gitee.usl.grammar.runtime.type.AviatorObject;
import com.gitee.usl.grammar.runtime.type.AviatorType;
import com.gitee.usl.grammar.utils.Env;
import com.gitee.usl.grammar.utils.Reflector;
import com.gitee.usl.grammar.utils.TypeUtils;


/**
 * is_a(x, clazz) returns true when x is an intance of the class.
 *
 * @author dennis
 *
 */
@SystemFunction
public class IsAFunction extends AbstractFunction {

  private static final long serialVersionUID = -7543895978170666671L;

  private IsAFunction() {}

  public static final IsAFunction INSTANCE = new IsAFunction();



  @Override
  public String getName() {
    return "is_a";
  }



  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2) {
    Object obj = arg1.getValue(env);

    if (obj == null) {
      return AviatorBoolean.FALSE;
    }


    if (arg2.getAviatorType() != AviatorType.JavaType) {
      throw new IllegalArgumentException("Invalid class type: " + arg2.desc(env));
    }
    try {
      Class<?> clazz = null;
      final String name = ((AviatorJavaType) arg2).getName();
      if (TypeUtils.PRIMITIVE_TYPES.containsKey(name)) {
        clazz = TypeUtils.PRIMITIVE_TYPES.get(name);
      } else {
        clazz = ((Env) env).resolveClassSymbol(name, false);
      }
      return AviatorBoolean.valueOf(clazz.isInstance(obj));
    } catch (ClassNotFoundException e) {
      throw Reflector.sneakyThrow(e);
    }
  }

}

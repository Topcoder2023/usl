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
package com.googlecode.aviator.runtime.function.seq;

import java.util.List;
import java.util.Map;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.ArrayUtils;


/**
 * seq.contains_key(map,key) function to check if seq(should be map) contains the key.
 *
 * @author dennis
 *
 */
public class SeqContainsKeyFunction extends AbstractFunction {


  private static final long serialVersionUID = 1543232112837279691L;


  @SuppressWarnings("rawtypes")
  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2) {
    Object first = arg1.getValue(env);
    if (first == null) {
      return AviatorBoolean.FALSE;
    }
    Class<?> clazz = first.getClass();
    if (Map.class.isAssignableFrom(clazz)) {
      Map seq = (Map) first;
      try {
        return AviatorBoolean.valueOf(seq.containsKey(arg2.getValue(env)));
      } catch (Exception e) {
        RuntimeUtils.printStackTrace(env, e);
        return AviatorBoolean.FALSE;
      }
    } else if (clazz.isArray()) {
      int index = FunctionUtils.getNumberValue(arg2, env).intValue();
      return AviatorBoolean.valueOf(index >= 0 && index < ArrayUtils.getLength(first));
    } else if (List.class.isAssignableFrom(clazz)) {
      int index = FunctionUtils.getNumberValue(arg2, env).intValue();
      return AviatorBoolean.valueOf(index >= 0 && index < ((List) first).size());
    } else {
      throw new IllegalArgumentException(arg1.desc(env) + " is not a map or array.");
    }
  }


  @Override
  public String getName() {
    return "seq.contains_key";
  }

}

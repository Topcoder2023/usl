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

import java.util.Map;
import com.googlecode.aviator.exception.FunctionNotFoundException;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import com.googlecode.aviator.runtime.type.AviatorJavaType;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.Collector;
import com.googlecode.aviator.runtime.type.Sequence;


/**
 * map(col,fun) function to iterate seq with function
 *
 * @author dennis
 *
 */
public class SeqMapFunction extends AbstractFunction {


  private static final long serialVersionUID = -4781893293207344881L;


  @Override
  @SuppressWarnings({"rawtypes"})
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2) {

    Object first = arg1.getValue(env);
    AviatorFunction fun = FunctionUtils.getFunction(arg2, env, 1);
    if (fun == null) {
      throw new FunctionNotFoundException(
          "There is no function named " + ((AviatorJavaType) arg2).getName());
    }
    if (first == null) {
      return AviatorNil.NIL;
    }
    Sequence seq = RuntimeUtils.seq(first, env);
    Collector collector = seq.newCollector(seq.hintSize());
    for (Object obj : seq) {
      collector.add(fun.call(env, AviatorRuntimeJavaType.valueOf(obj)).getValue(env));
    }
    return AviatorRuntimeJavaType.valueOf(collector.getRawContainer());
  }


  @Override
  public String getName() {
    return "map";
  }

}

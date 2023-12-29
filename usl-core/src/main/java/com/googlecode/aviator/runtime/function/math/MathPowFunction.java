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
package com.googlecode.aviator.runtime.function.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBigInt;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.utils.TypeUtils;


/**
 * math.pow(d1,d2) function
 *
 * @author dennis
 *
 */
public class MathPowFunction extends AbstractFunction {


  private static final long serialVersionUID = 5909888819672336251L;

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1,
      final AviatorObject arg2) {
    Number left = FunctionUtils.getNumberValue(arg1, env);
    Number right = FunctionUtils.getNumberValue(arg2, env);
    if (TypeUtils.isBigInt(left)) {
      return new AviatorBigInt(((BigInteger) left).pow(right.intValue()));
    } else if (TypeUtils.isDecimal(left)) {
      return new AviatorDecimal(
          ((BigDecimal) left).pow(right.intValue(), RuntimeUtils.getMathContext(env)));
    } else {
      return new AviatorDouble(Math.pow(left.doubleValue(), right.doubleValue()));
    }

  }

  @Override
  public String getName() {
    return "math.pow";
  }
}

package com.googlecode.aviator.runtime.function.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;


/**
 * string_to_date function
 * 
 * @author dennis(killme2008@gmail.com)
 * 
 */
public class String2DateFunction extends AbstractFunction {


  private static final long serialVersionUID = -1899407811376304410L;


  @Override
  public String getName() {
    return "string_to_date";
  }


  @Override
  public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
    String source = FunctionUtils.getStringValue(arg1, env);
    String format = FunctionUtils.getStringValue(arg2, env);
    SimpleDateFormat dateFormat = DateFormatCache.getOrCreateDateFormat(format);
    try {
      return AviatorRuntimeJavaType.valueOf(dateFormat.parse(source));
    } catch (ParseException e) {
      throw new ExpressionRuntimeException("Cast string to date failed", e);
    }
  }

}

package com.googlecode.aviator.runtime.function.string;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;

import java.util.Map;


/**
 * string.replace_first function
 * 
 * @author dennis(killme2008@gmail.com)
 * 
 */
public class StringReplaceFirstFunction extends AbstractFunction {

  private static final long serialVersionUID = 1563485375844407804L;


  @Override
  public String getName() {
    return "string.replace_first";
  }


  @Override
  public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2,
      AviatorObject arg3) {
    String target = FunctionUtils.getStringValue(arg1, env);
    String regex = FunctionUtils.getStringValue(arg2, env);
    String replacement = FunctionUtils.getStringValue(arg3, env);
    return new AviatorString(target.replaceFirst(regex, replacement));

  }

}

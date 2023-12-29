package com.googlecode.aviator.runtime.function.seq;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorNil;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;

import java.util.Map;


/**
 * seq.vals(map) to retrieve values sequence of the map.
 *
 * @since 5.2.0
 * @author dennis
 *
 */
public class SeqValsFunction extends AbstractFunction {


  private static final long serialVersionUID = -8707187642296260032L;

  private SeqValsFunction() {

  }

  public static final SeqValsFunction INSTANCE = new SeqValsFunction();

  @Override
  public String getName() {
    return "seq.vals";
  }

  @SuppressWarnings("rawtypes")
  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {

    Map m = (Map) arg1.getValue(env);

    if (m == null) {
      return AviatorNil.NIL;
    }

    return AviatorRuntimeJavaType.valueOf(m.values());
  }

}

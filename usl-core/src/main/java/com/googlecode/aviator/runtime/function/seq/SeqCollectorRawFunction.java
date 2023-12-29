package com.googlecode.aviator.runtime.function.seq;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorRuntimeJavaType;
import com.googlecode.aviator.runtime.type.Collector;

import java.util.Map;

/**
 * seq.raw(collector) to retrieve the raw container of collector.
 *
 * @author dennis(killme2008@gmail.com)
 *
 */
public class SeqCollectorRawFunction extends AbstractFunction {

  private static final long serialVersionUID = -3174913891253579826L;

  private SeqCollectorRawFunction() {}

  public static final SeqCollectorRawFunction INSTANCE = new SeqCollectorRawFunction();

  @Override
  public AviatorObject call(final Map<String, Object> env, final AviatorObject arg1) {

    return AviatorRuntimeJavaType.valueOf(((Collector) arg1.getValue(env)).getRawContainer());
  }

  @Override
  public String getName() {
    return "seq.raw";
  }

}

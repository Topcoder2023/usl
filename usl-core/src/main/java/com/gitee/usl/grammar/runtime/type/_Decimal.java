package com.gitee.usl.grammar.runtime.type;

import java.math.BigDecimal;
import java.util.Map;
import com.gitee.usl.grammar.ScriptEngine;
import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.gitee.usl.grammar.Options;


/**
 * Aviator Big Decimal
 *
 * @since 2.3.0
 * @author dennis<killme2008@gmail.com>
 *
 */
public class _Decimal extends _Number {


  private static final long serialVersionUID = 7084583813460322882L;


  public _Decimal(final Number number) {
    super(number);
  }


  public static final _Decimal valueOf(final BigDecimal d) {
    return new _Decimal(d);
  }


  public static final _Decimal valueOf(final Map<String, Object> env, final String d) {
    return new _Decimal(new BigDecimal(d, RuntimeUtils.getMathContext(env)));
  }


  public static final _Decimal valueOf(final ScriptEngine instance,
                                       final String d) {
    return new _Decimal(
        new BigDecimal(d, instance.getOptionValue(Options.MATH_CONTEXT).mathContext));
  }

  @Override
  public _Object innerSub(final Map<String, Object> env, final _Number other) {
    if (other.getAviatorType() != _Type.Double) {
      return _Decimal
          .valueOf(toDecimal(env).subtract(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
    } else {
      return _Double.valueOf(doubleValue() - other.doubleValue());
    }
  }


  @Override
  public _Object neg(final Map<String, Object> env) {
    return _Decimal.valueOf(toDecimal(env).negate());
  }


  @Override
  public _Object innerMult(final Map<String, Object> env, final _Number other) {
    if (other.getAviatorType() != _Type.Double) {
      return _Decimal
          .valueOf(toDecimal(env).multiply(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
    } else {
      return _Double.valueOf(doubleValue() * other.doubleValue());
    }
  }


  @Override
  public _Object innerMod(final Map<String, Object> env, final _Number other) {
    if (other.getAviatorType() != _Type.Double) {
      return _Decimal.valueOf(
          toDecimal(env).remainder(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
    } else {
      return _Double.valueOf(doubleValue() % other.doubleValue());
    }
  }


  @Override
  public _Object innerDiv(final Map<String, Object> env, final _Number other) {
    if (other.getAviatorType() != _Type.Double) {
      return _Decimal
          .valueOf(toDecimal(env).divide(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
    } else {
      return _Double.valueOf(doubleValue() / other.doubleValue());
    }
  }


  @Override
  public _Number innerAdd(final Map<String, Object> env, final _Number other) {
    if (other.getAviatorType() != _Type.Double) {
      return _Decimal
          .valueOf(toDecimal(env).add(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
    } else {
      return _Double.valueOf(doubleValue() + other.doubleValue());
    }
  }


  @Override
  public int innerCompare(final Map<String, Object> env, final _Number other) {
    if (other.getAviatorType() != _Type.Double) {
      return toDecimal(env).compareTo(other.toDecimal(env));
    } else {
      return Double.compare(doubleValue(), other.doubleValue());
    }
  }


  @Override
  public _Type getAviatorType() {
    return _Type.Decimal;
  }

}

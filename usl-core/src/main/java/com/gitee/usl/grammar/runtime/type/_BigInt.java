package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.runtime.RuntimeUtils;

import java.math.BigInteger;
import java.util.Map;


/**
 * Aviator Big Integer
 *
 * @since 2.3.0
 * @author dennis<killme2008@gmail.com>
 *
 */
public class _BigInt extends _Long {


  private static final long serialVersionUID = 2208761926142343652L;


  private static class BigIntCache {
    private BigIntCache() {}

    static final _BigInt cache[] = new _BigInt[256];

    static {
      for (long i = 0; i < cache.length; i++) {
        cache[(int) i] = new _BigInt(BigInteger.valueOf(i - 128));
      }
    }
  }



  @Override
  public Object getValue(Map<String, Object> env) {
    return this.number;
  }


  @Override
  public long longValue() {
    return this.number.longValue();
  }


  @Override
  public double doubleValue() {
    return this.number.doubleValue();
  }


  public _BigInt(Number number) {
    super(number);
  }


  public static final _BigInt valueOf(BigInteger v) {
    return new _BigInt(v);
  }


  public static final _BigInt valueOf(String v) {
    return new _BigInt(new BigInteger(v));
  }


  public static final _BigInt valueOf(long l) {
    final int offset = 128;
    if (l >= -128 && l <= 127) {
      return BigIntCache.cache[(int) l + offset];
    }
    return valueOf(BigInteger.valueOf(l));
  }


  @Override
  public _Object neg(Map<String, Object> env) {
    return _BigInt.valueOf(this.toBigInt().negate());
  }


  @Override
  public _Object innerSub(Map<String, Object> env, _Number other) {
    switch (other.getAviatorType()) {
      case Decimal:
        return _Decimal.valueOf(
            this.toDecimal(env).subtract(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
      case Double:
        return _Double.valueOf(this.doubleValue() - other.doubleValue());
      default:
        return _BigInt.valueOf(this.toBigInt().subtract(other.toBigInt()));
    }
  }


  @Override
  public _Object innerMult(Map<String, Object> env, _Number other) {
    switch (other.getAviatorType()) {
      case Decimal:
        return _Decimal.valueOf(
            this.toDecimal(env).multiply(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
      case Double:
        return _Double.valueOf(this.doubleValue() * other.doubleValue());
      default:
        return _BigInt.valueOf(this.toBigInt().multiply(other.toBigInt()));
    }
  }


  @Override
  public _Object innerMod(Map<String, Object> env, _Number other) {
    switch (other.getAviatorType()) {
      case Decimal:
        return _Decimal.valueOf(
            this.toDecimal(env).remainder(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
      case Double:
        return _Double.valueOf(this.doubleValue() % other.doubleValue());
      default:
        return _BigInt.valueOf(this.toBigInt().mod(other.toBigInt()));
    }
  }


  @Override
  public _Object innerDiv(Map<String, Object> env, _Number other) {
    switch (other.getAviatorType()) {
      case Decimal:
        return _Decimal.valueOf(
            this.toDecimal(env).divide(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
      case Double:
        return _Double.valueOf(this.doubleValue() / other.doubleValue());
      default:
        return _BigInt.valueOf(this.toBigInt().divide(other.toBigInt()));
    }
  }


  @Override
  public _Number innerAdd(Map<String, Object> env, _Number other) {
    switch (other.getAviatorType()) {
      case Decimal:
        return _Decimal.valueOf(
            this.toDecimal(env).add(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
      case Double:
        return _Double.valueOf(this.doubleValue() + other.doubleValue());
      default:
        return _BigInt.valueOf(this.toBigInt().add(other.toBigInt()));
    }
  }


  @Override
  public int innerCompare(Map<String, Object> env, _Number other) {
    switch (other.getAviatorType()) {
      case Decimal:
        return this.toDecimal(env).compareTo(other.toDecimal(env));
      case Double:
        return Double.compare(this.doubleValue(), other.doubleValue());
      default:
        return this.toBigInt().compareTo(other.toBigInt());
    }
  }


  @Override
  protected _Object innerBitAnd(_Object other) {
    return _BigInt.valueOf(this.toBigInt().and(((_Number) other).toBigInt()));
  }


  @Override
  protected _Object innerBitOr(_Object other) {
    return _BigInt.valueOf(this.toBigInt().or(((_Number) other).toBigInt()));
  }


  @Override
  protected _Object innerBitXor(_Object other) {
    return _BigInt.valueOf(this.toBigInt().xor(((_Number) other).toBigInt()));
  }


  @Override
  protected _Object innerShiftLeft(_Object other) {
    this.ensureLong(other);
    return _BigInt
        .valueOf(this.toBigInt().shiftLeft((int) ((_Number) other).longValue()));
  }


  @Override
  protected _Object innerShiftRight(_Object other) {
    this.ensureLong(other);
    return _BigInt
        .valueOf(this.toBigInt().shiftRight((int) ((_Number) other).longValue()));
  }


  @Override
  protected _Object innerUnsignedShiftRight(_Object other) {
    return this.innerShiftRight(other);
  }


  @Override
  public _Type getAviatorType() {
    return _Type.BigInt;
  }

}

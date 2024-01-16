package com.gitee.usl.grammar.runtime.type;

import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.gitee.usl.grammar.utils.TypeUtils;

import java.util.Map;


/**
 * @author hongda.li
 */
public class _Long extends _Number {

    private static class LongCache {
        private LongCache() {
        }

        static final _Long cache[] = new _Long[256];

        static {
            for (long i = 0; i < cache.length; i++) {
                cache[(int) i] = new _Long(i - 128);
            }
        }
    }

    _Long(final long i) {
        super(i);
    }


    _Long(final Number number) {
        super(number);

    }


    public static _Long valueOf(final long l) {
        final int offset = 128;
        if (l >= -128 && l <= 127) { // will cache
            return LongCache.cache[(int) l + offset];
        }
        return new _Long(l);
    }


    public static _Long valueOf(final Long l) {
        return valueOf(l.longValue());
    }


    @Override
    public _Object neg(final Map<String, Object> env) {
        return _Long.valueOf(-this.longValue);
    }


    @Override
    public int innerCompare(final Map<String, Object> env, final _Number other) {
        if (other.getAviatorType() == _Type.Long) {
            return TypeUtils.comapreLong(longValue(), other.longValue());
        }

        switch (other.getAviatorType()) {
            case BigInt:
                return toBigInt().compareTo(other.toBigInt());
            case Decimal:
                return toDecimal(env).compareTo(other.toDecimal(env));
            case Double:
                return Double.compare(doubleValue(), other.doubleValue());
            default:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with " + other.desc(env));
        }
    }


    @Override
    public _Object innerDiv(final Map<String, Object> env, final _Number other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return _BigInt.valueOf(toBigInt().divide(other.toBigInt()));
            case Decimal:
                return _Decimal
                        .valueOf(toDecimal(env).divide(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long:
                return _Long.valueOf(this.longValue / other.longValue());
            default:
                return _Double.valueOf(this.longValue / other.doubleValue());
        }
    }


    @Override
    public _Object innerAdd(final Map<String, Object> env, final _Number other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return _BigInt.valueOf(toBigInt().add(other.toBigInt()));
            case Decimal:
                return _Decimal
                        .valueOf(toDecimal(env).add(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long:
                return _Long.valueOf(this.longValue + other.longValue());
            default:
                return new _Double(this.longValue + other.doubleValue());
        }
    }


    @Override
    public _Object innerMod(final Map<String, Object> env, final _Number other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return _BigInt.valueOf(toBigInt().mod(other.toBigInt()));
            case Decimal:
                return _Decimal.valueOf(
                        toDecimal(env).remainder(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long:
                return _Long.valueOf(this.longValue % other.longValue());
            default:
                return new _Double(this.longValue % other.doubleValue());
        }
    }


    @Override
    public _Object innerMult(final Map<String, Object> env, final _Number other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return _BigInt.valueOf(toBigInt().multiply(other.toBigInt()));
            case Decimal:
                return _Decimal.valueOf(
                        toDecimal(env).multiply(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long:
                return _Long.valueOf(this.longValue * other.longValue());
            default:
                return new _Double(this.longValue * other.doubleValue());
        }
    }


    protected void ensureLong(final _Object other) {
        if (other.getAviatorType() != _Type.Long) {
            throw new ExpressionRuntimeException(
                    other + " is not long type,could not be used as a bit operand.");
        }
    }


    @Override
    public _Object bitAnd(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitAnd(other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitAnd(_Number.valueOf(otherValue));
                } else {
                    return super.bitAnd(other, env);
                }
            default:
                return super.bitAnd(other, env);
        }
    }


    protected _Object innerBitAnd(final _Object other) {
        ensureLong(other);
        _Long otherLong = (_Long) other;
        return _Long.valueOf(this.longValue & otherLong.longValue());
    }


    protected _Object innerBitOr(final _Object other) {
        ensureLong(other);
        _Long otherLong = (_Long) other;
        return _Long.valueOf(this.longValue | otherLong.longValue());
    }


    protected _Object innerBitXor(final _Object other) {
        ensureLong(other);
        _Long otherLong = (_Long) other;
        return _Long.valueOf(this.longValue ^ otherLong.longValue());
    }


    protected _Object innerShiftLeft(final _Object other) {
        ensureLong(other);
        _Long otherLong = (_Long) other;
        return _Long.valueOf(this.longValue << otherLong.longValue());
    }


    protected _Object innerShiftRight(final _Object other) {
        ensureLong(other);
        _Long otherLong = (_Long) other;
        return _Long.valueOf(this.longValue >> otherLong.longValue());
    }


    protected _Object innerUnsignedShiftRight(final _Object other) {
        ensureLong(other);
        _Long otherLong = (_Long) other;
        return _Long.valueOf(this.longValue >>> otherLong.longValue());
    }


    @Override
    public _Object bitNot(final Map<String, Object> env) {
        return _Long.valueOf(~this.longValue);
    }


    @Override
    public Object getValue(final Map<String, Object> env) {
        return this.longValue;
    }


    @Override
    public long longValue() {
        return this.longValue;
    }


    @Override
    public double doubleValue() {
        return this.longValue;
    }


    @Override
    public _Object bitOr(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitOr(other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitOr(_Number.valueOf(otherValue));
                } else {
                    return super.bitOr(other, env);
                }
            default:
                return super.bitOr(other, env);
        }
    }


    @Override
    public _Object bitXor(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitXor(other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitXor(_Number.valueOf(otherValue));
                } else {
                    return super.bitXor(other, env);
                }
            default:
                return super.bitXor(other, env);
        }
    }


    @Override
    public _Object shiftLeft(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerShiftLeft(other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerShiftLeft(_Number.valueOf(otherValue));
                } else {
                    return super.shiftLeft(other, env);
                }
            default:
                return super.shiftLeft(other, env);
        }
    }


    @Override
    public _Object shiftRight(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerShiftRight(other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerShiftRight(_Number.valueOf(otherValue));
                } else {
                    return super.shiftRight(other, env);
                }
            default:
                return super.shiftRight(other, env);
        }
    }


    @Override
    public _Object unsignedShiftRight(final _Object other,
                                      final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerUnsignedShiftRight(other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerUnsignedShiftRight(_Number.valueOf(otherValue));
                } else {
                    return super.unsignedShiftRight(other, env);
                }
            default:
                return super.unsignedShiftRight(other, env);
        }
    }


    @Override
    public _Object innerSub(final Map<String, Object> env, final _Number other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return _BigInt.valueOf(toBigInt().subtract(other.toBigInt()));
            case Decimal:
                return _Decimal.valueOf(
                        toDecimal(env).subtract(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long:
                return _Long.valueOf(this.longValue - other.longValue());
            default:
                return new _Double(this.longValue - other.doubleValue());
        }
    }


    @Override
    public _Type getAviatorType() {
        return _Type.Long;
    }

}

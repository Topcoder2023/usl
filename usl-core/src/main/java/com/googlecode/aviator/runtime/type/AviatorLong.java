package com.googlecode.aviator.runtime.type;

import com.gitee.usl.grammar.type.USLBigInt;
import com.gitee.usl.grammar.type.USLObject;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.googlecode.aviator.exception.ExpressionRuntimeException;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.utils.TypeUtils;

import java.util.Map;

/**
 * @author hongda.li
 */
public class AviatorLong extends AviatorNumber {

    public AviatorLong(final long i) {
        super(i);
    }


    public AviatorLong(final Number number) {
        super(number);
    }

    public static AviatorLong valueOf(final long l) {
        return new AviatorLong(l);
    }

    public static AviatorLong valueOf(final Long l) {
        return valueOf(l.longValue());
    }


    @Override
    public USLObject neg(final Map<String, Object> env) {
        return AviatorLong.valueOf(-this.longValue);
    }


    @Override
    public int innerCompare(final Map<String, Object> env, final AviatorNumber other) {
        if (other.getAviatorType() == AviatorType.Long) {
            return TypeUtils.comapreLong(longValue(), other.longValue());
        }

        return switch (other.getAviatorType()) {
            case BigInt -> toBigInt().compareTo(other.toBigInt());
            case Decimal -> toDecimal(env).compareTo(other.toDecimal(env));
            case Double -> Double.compare(doubleValue(), other.doubleValue());
            default -> throw new CompareNotSupportedException(
                    "Could not compare " + desc(env) + " with " + other.desc(env));
        };
    }


    @Override
    public USLObject innerDiv(final Map<String, Object> env, final AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case BigInt -> USLBigInt.valueOf(toBigInt().divide(other.toBigInt()));
            case Decimal -> AviatorDecimal
                    .valueOf(toDecimal(env).divide(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long -> AviatorLong.valueOf(this.longValue / other.longValue());
            default -> AviatorDouble.valueOf(this.longValue / other.doubleValue());
        };
    }


    @Override
    public USLObject innerAdd(final Map<String, Object> env, final AviatorNumber other) {
        switch (other.getAviatorType()) {
            case BigInt:
                return USLBigInt.valueOf(toBigInt().add(other.toBigInt()));
            case Decimal:
                return AviatorDecimal
                        .valueOf(toDecimal(env).add(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long:
                return AviatorLong.valueOf(this.longValue + other.longValue());
            default:
                return new AviatorDouble(this.longValue + other.doubleValue());
        }
    }


    @Override
    public USLObject innerMod(final Map<String, Object> env, final AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case BigInt -> USLBigInt.valueOf(toBigInt().mod(other.toBigInt()));
            case Decimal -> AviatorDecimal.valueOf(
                    toDecimal(env).remainder(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long -> AviatorLong.valueOf(this.longValue % other.longValue());
            default -> new AviatorDouble(this.longValue % other.doubleValue());
        };
    }


    @Override
    public USLObject innerMult(final Map<String, Object> env, final AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case BigInt -> USLBigInt.valueOf(toBigInt().multiply(other.toBigInt()));
            case Decimal -> AviatorDecimal.valueOf(
                    toDecimal(env).multiply(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long -> AviatorLong.valueOf(this.longValue * other.longValue());
            default -> new AviatorDouble(this.longValue * other.doubleValue());
        };
    }


    protected void ensureLong(final USLObject other) {
        if (other.getAviatorType() != AviatorType.Long) {
            throw new ExpressionRuntimeException(
                    other + " is not long type,could not be used as a bit operand.");
        }
    }


    @Override
    public USLObject bitAnd(final USLObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitAnd(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitAnd(AviatorNumber.valueOf(otherValue));
                } else {
                    return super.bitAnd(other, env);
                }
            default:
                return super.bitAnd(other, env);
        }
    }


    protected USLObject innerBitAnd(final USLObject other) {
        ensureLong(other);
        AviatorLong otherLong = (AviatorLong) other;
        return AviatorLong.valueOf(this.longValue & otherLong.longValue());
    }


    protected USLObject innerBitOr(final USLObject other) {
        ensureLong(other);
        AviatorLong otherLong = (AviatorLong) other;
        return AviatorLong.valueOf(this.longValue | otherLong.longValue());
    }


    protected USLObject innerBitXor(final USLObject other) {
        ensureLong(other);
        AviatorLong otherLong = (AviatorLong) other;
        return AviatorLong.valueOf(this.longValue ^ otherLong.longValue());
    }


    protected USLObject innerShiftLeft(final USLObject other) {
        ensureLong(other);
        AviatorLong otherLong = (AviatorLong) other;
        return AviatorLong.valueOf(this.longValue << otherLong.longValue());
    }


    protected USLObject innerShiftRight(final USLObject other) {
        ensureLong(other);
        AviatorLong otherLong = (AviatorLong) other;
        return AviatorLong.valueOf(this.longValue >> otherLong.longValue());
    }


    protected USLObject innerUnsignedShiftRight(final USLObject other) {
        ensureLong(other);
        AviatorLong otherLong = (AviatorLong) other;
        return AviatorLong.valueOf(this.longValue >>> otherLong.longValue());
    }


    @Override
    public USLObject bitNot(final Map<String, Object> env) {
        return AviatorLong.valueOf(~this.longValue);
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
    public USLObject bitOr(final USLObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitOr(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitOr(AviatorNumber.valueOf(otherValue));
                } else {
                    return super.bitOr(other, env);
                }
            default:
                return super.bitOr(other, env);
        }
    }


    @Override
    public USLObject bitXor(final USLObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerBitXor(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerBitXor(AviatorNumber.valueOf(otherValue));
                } else {
                    return super.bitXor(other, env);
                }
            default:
                return super.bitXor(other, env);
        }
    }


    @Override
    public USLObject shiftLeft(final USLObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerShiftLeft(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerShiftLeft(AviatorNumber.valueOf(otherValue));
                } else {
                    return super.shiftLeft(other, env);
                }
            default:
                return super.shiftLeft(other, env);
        }
    }


    @Override
    public USLObject shiftRight(final USLObject other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerShiftRight(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerShiftRight(AviatorNumber.valueOf(otherValue));
                } else {
                    return super.shiftRight(other, env);
                }
            default:
                return super.shiftRight(other, env);
        }
    }


    @Override
    public USLObject unsignedShiftRight(final USLObject other,
                                        final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerUnsignedShiftRight(other);
            case JavaType:
                AviatorJavaType otherJavaType = (AviatorJavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerUnsignedShiftRight(AviatorNumber.valueOf(otherValue));
                } else {
                    return super.unsignedShiftRight(other, env);
                }
            default:
                return super.unsignedShiftRight(other, env);
        }
    }


    @Override
    public USLObject innerSub(final Map<String, Object> env, final AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case BigInt -> USLBigInt.valueOf(toBigInt().subtract(other.toBigInt()));
            case Decimal -> AviatorDecimal.valueOf(
                    toDecimal(env).subtract(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Long -> AviatorLong.valueOf(this.longValue - other.longValue());
            default -> new AviatorDouble(this.longValue - other.doubleValue());
        };
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.Long;
    }

}

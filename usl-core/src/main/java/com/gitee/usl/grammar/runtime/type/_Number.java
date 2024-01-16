package com.gitee.usl.grammar.runtime.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import com.gitee.usl.grammar.runtime.RuntimeUtils;
import com.googlecode.aviator.exception.CompareNotSupportedException;
import com.gitee.usl.grammar.utils.TypeUtils;

/**
 * @author hongda.li
 *
 */
public abstract class _Number extends _Object {
    /**
     * Number union
     */
    // Only for bigint/decimal
    protected Number number;
    // Only valid for AviatorLong
    protected long longValue;
    // Only valid for AviatorDouble
    protected double doubleValue;


    public _Number(final long longValue) {
        super();
        this.longValue = longValue;
    }


    public _Number(final double doubleValue) {
        super();
        this.doubleValue = doubleValue;
    }


    public _Number(final Number number) {
        super();
        this.number = number;
    }


    @Override
    public Object getValue(final Map<String, Object> env) {
        return this.number;
    }


    public static _Number valueOf(final Object value) {
        if (TypeUtils.isLong(value)) {
            return _Long.valueOf(((Number) value).longValue());
        } else if (TypeUtils.isDouble(value)) {
            return new _Double(((Number) value).doubleValue());
        } else if (TypeUtils.isBigInt(value)) {
            return _BigInt.valueOf((BigInteger) value);
        } else if (TypeUtils.isDecimal(value)) {
            return _Decimal.valueOf((BigDecimal) value);
        } else {
            throw new ClassCastException("Could not cast " + value.getClass().getName() + " to Number");
        }

    }


    public double doubleValue() {
        return this.number.doubleValue();
    }


    @Override
    public _Object add(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case String:
                return new _String(getValue(env).toString() + ((_String) other).getLexeme(env));
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerAdd(env, (_Number) other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerAdd(env, _Number.valueOf(otherValue));
                } else if (TypeUtils.isString(otherValue)) {
                    return new _String(getValue(env).toString() + otherValue);
                } else {
                    return super.add(other, env);
                }
            default:
                return super.add(other, env);
        }

    }


    @Override
    public _Object sub(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerSub(env, (_Number) other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerSub(env, _Number.valueOf(otherValue));
                } else {
                    return super.sub(other, env);
                }
            default:
                return super.sub(other, env);
        }

    }


    @Override
    public _Object mod(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerMod(env, (_Number) other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerMod(env, _Number.valueOf(otherValue));
                } else {
                    return super.mod(other, env);
                }
            default:
                return super.mod(other, env);
        }
    }


    @Override
    public _Object div(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerDiv(env, (_Number) other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerDiv(env, _Number.valueOf(otherValue));
                } else {
                    return super.div(other, env);
                }
            default:
                return super.div(other, env);
        }

    }


    @Override
    public _Object mult(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerMult(env, (_Number) other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue instanceof Number) {
                    return innerMult(env, _Number.valueOf(otherValue));
                } else {
                    return super.mult(other, env);
                }
            default:
                return super.mult(other, env);
        }

    }


    @Override
    public int innerCompare(final _Object other, final Map<String, Object> env) {
        switch (other.getAviatorType()) {
            case BigInt:
            case Decimal:
            case Long:
            case Double:
                return innerCompare(env, (_Number) other);
            case JavaType:
                _JavaType otherJavaType = (_JavaType) other;
                final Object otherValue = otherJavaType.getValue(env);
                if (otherValue == null) {
                    return 1;
                }
                if (otherValue instanceof Number) {
                    return innerCompare(env, _Number.valueOf(otherValue));
                } else {
                    throw new CompareNotSupportedException(
                            "Could not compare " + desc(env) + " with " + other.desc(env));
                }
            case Nil:
                return 1;
            default:
                throw new CompareNotSupportedException(
                        "Could not compare " + desc(env) + " with " + other.desc(env));

        }
    }


    public abstract _Object innerSub(Map<String, Object> env, _Number other);


    public abstract _Object innerMult(Map<String, Object> env, _Number other);


    public abstract _Object innerMod(Map<String, Object> env, _Number other);


    public abstract _Object innerDiv(Map<String, Object> env, _Number other);


    public abstract _Object innerAdd(Map<String, Object> env, _Number other);


    public abstract int innerCompare(Map<String, Object> env, _Number other);


    public long longValue() {
        return this.number.longValue();
    }


    public final BigInteger toBigInt() {
        if (TypeUtils.isBigInt(this.number)) {
            return (BigInteger) this.number;
        } else {
            return new BigInteger(String.valueOf(longValue()));
        }
    }


    public final BigDecimal toDecimal(final Map<String, Object> env) {
        if (TypeUtils.isDecimal(this.number)) {
            return (BigDecimal) this.number;
        } else if (TypeUtils.isBigInt(this.number)) {
            return new BigDecimal(toBigInt());
        } else {
            return new BigDecimal(doubleValue(), RuntimeUtils.getMathContext(env));
        }
    }
}

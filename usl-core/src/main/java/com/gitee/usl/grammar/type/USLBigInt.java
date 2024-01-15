package com.gitee.usl.grammar.type;

import com.gitee.usl.api.annotation.AsmMethod;
import com.googlecode.aviator.runtime.RuntimeUtils;
import com.googlecode.aviator.runtime.type.*;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author hongda.li
 */
public class USLBigInt extends AviatorLong {

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

    public USLBigInt(Number number) {
        super(number);
    }

    @AsmMethod
    public static USLBigInt valueOf(BigInteger v) {
        return new USLBigInt(v);
    }

    @AsmMethod
    public static USLBigInt valueOf(String v) {
        return new USLBigInt(new BigInteger(v));
    }

    @AsmMethod
    public static USLBigInt valueOf(long l) {
        return valueOf(BigInteger.valueOf(l));
    }

    @Override
    public USLObject neg(Map<String, Object> env) {
        return USLBigInt.valueOf(this.toBigInt().negate());
    }


    @Override
    public USLObject innerSub(Map<String, Object> env, AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case Decimal -> AviatorDecimal.valueOf(
                    this.toDecimal(env).subtract(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Double -> AviatorDouble.valueOf(this.doubleValue() - other.doubleValue());
            default -> USLBigInt.valueOf(this.toBigInt().subtract(other.toBigInt()));
        };
    }


    @Override
    public USLObject innerMult(Map<String, Object> env, AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case Decimal -> AviatorDecimal.valueOf(
                    this.toDecimal(env).multiply(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Double -> AviatorDouble.valueOf(this.doubleValue() * other.doubleValue());
            default -> USLBigInt.valueOf(this.toBigInt().multiply(other.toBigInt()));
        };
    }


    @Override
    public USLObject innerMod(Map<String, Object> env, AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case Decimal -> AviatorDecimal.valueOf(
                    this.toDecimal(env).remainder(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Double -> AviatorDouble.valueOf(this.doubleValue() % other.doubleValue());
            default -> USLBigInt.valueOf(this.toBigInt().mod(other.toBigInt()));
        };
    }


    @Override
    public USLObject innerDiv(Map<String, Object> env, AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case Decimal -> AviatorDecimal.valueOf(
                    this.toDecimal(env).divide(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Double -> AviatorDouble.valueOf(this.doubleValue() / other.doubleValue());
            default -> USLBigInt.valueOf(this.toBigInt().divide(other.toBigInt()));
        };
    }


    @Override
    public AviatorNumber innerAdd(Map<String, Object> env, AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case Decimal -> AviatorDecimal.valueOf(
                    this.toDecimal(env).add(other.toDecimal(env), RuntimeUtils.getMathContext(env)));
            case Double -> AviatorDouble.valueOf(this.doubleValue() + other.doubleValue());
            default -> USLBigInt.valueOf(this.toBigInt().add(other.toBigInt()));
        };
    }


    @Override
    public int innerCompare(Map<String, Object> env, AviatorNumber other) {
        return switch (other.getAviatorType()) {
            case Decimal -> this.toDecimal(env).compareTo(other.toDecimal(env));
            case Double -> Double.compare(this.doubleValue(), other.doubleValue());
            default -> this.toBigInt().compareTo(other.toBigInt());
        };
    }


    @Override
    protected USLObject innerBitAnd(USLObject other) {
        return USLBigInt.valueOf(this.toBigInt().and(((AviatorNumber) other).toBigInt()));
    }


    @Override
    protected USLObject innerBitOr(USLObject other) {
        return USLBigInt.valueOf(this.toBigInt().or(((AviatorNumber) other).toBigInt()));
    }


    @Override
    protected USLObject innerBitXor(USLObject other) {
        return USLBigInt.valueOf(this.toBigInt().xor(((AviatorNumber) other).toBigInt()));
    }


    @Override
    protected USLObject innerShiftLeft(USLObject other) {
        this.ensureLong(other);
        return USLBigInt
                .valueOf(this.toBigInt().shiftLeft((int) ((AviatorNumber) other).longValue()));
    }


    @Override
    protected USLObject innerShiftRight(USLObject other) {
        this.ensureLong(other);
        return USLBigInt
                .valueOf(this.toBigInt().shiftRight((int) ((AviatorNumber) other).longValue()));
    }


    @Override
    protected USLObject innerUnsignedShiftRight(USLObject other) {
        return this.innerShiftRight(other);
    }


    @Override
    public AviatorType getAviatorType() {
        return AviatorType.BigInt;
    }

}

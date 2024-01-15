package com.gitee.usl.infra.structure;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.api.annotation.Description;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author hongda.li
 */
@Description("基础转换器")
public interface BasicConverter {

    Object origin();

    default String asStr() {
        return Convert.toStr(this.origin());
    }

    default Integer asInt() {
        return Convert.toInt(this.origin());
    }

    default Long asLong() {
        return Convert.toLong(this.origin());
    }

    default Boolean asBool() {
        return Convert.toBool(this.origin());
    }

    default Character asChar() {
        return Convert.toChar(this.origin());
    }

    default Float asFloat() {
        return Convert.toFloat(this.origin());
    }

    default Double asDouble() {
        return Convert.toDouble(this.origin());
    }

    default Number asNumber() {
        return Convert.toNumber(this.origin());
    }

    default Short asShort() {
        return Convert.toShort(this.origin());
    }

    default Byte asByte() {
        return Convert.toByte(this.origin());
    }

    default BigDecimal asBigDecimal() {
        return Convert.toBigDecimal(this.origin());
    }

    default BigInteger asBigInteger() {
        return Convert.toBigInteger(this.origin());
    }

    default <E extends Enum<E>> E asEnum(Class<E> clazz) {
        return Convert.toEnum(clazz, this.origin());
    }

    default Date asDate() {
        return Convert.toDate(this.origin());
    }

    default <T> T asType(Class<T> type) {
        return Convert.convert(type, this.origin());
    }

    default <T> List<T> asList(Class<T> type) {
        return Convert.toList(type, this.origin());
    }

    default <T> Set<T> asSet(Class<T> type) {
        return Convert.toSet(type, this.origin());
    }

    default <E> E asAny(Function<Object, E> function) {
        try {
            return function.apply(this.origin());
        } catch (Exception e) {
            return null;
        }
    }

}

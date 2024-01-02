package com.gitee.usl.infra.structure;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.BasicTypeGetter;
import cn.hutool.core.getter.OptBasicTypeGetter;
import cn.hutool.core.util.StrUtil;
import com.gitee.usl.api.annotation.Description;
import com.gitee.usl.infra.constant.NumberConstant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.hutool.core.map.MapUtil.DEFAULT_LOAD_FACTOR;

/**
 * @author hongda.li
 */
@Description("String类型作为唯一键的Map结构，并扩展多种快速转换返回值方法")
public class StringMap<V> extends LinkedHashMap<String, V> implements BasicTypeGetter<String>, OptBasicTypeGetter<String> {
    public StringMap() {
        this(NumberConstant.COMMON_SIZE);
    }

    public StringMap(int size) {
        super(size);
    }

    public StringMap(Map<String, V> otherMap) {
        super((int) (otherMap.size() / DEFAULT_LOAD_FACTOR) + 1);
        this.putAll(otherMap);
    }

    @Override
    public Object getObj(String key, Object defaultValue) {
        V val = this.get(key);
        if (val == null) {
            return defaultValue;
        } else {
            return val;
        }
    }

    @Override
    public Object getObj(String key) {
        return getObj(key, null);
    }

    @Override
    public String getStr(String key, String defaultValue) {
        return Convert.toStr(getStr(key), defaultValue);
    }

    @Override
    public String getStr(String key) {
        return getStr(key, null);
    }

    @Override
    public Integer getInt(String key, Integer defaultValue) {
        return Convert.toInt(getStr(key), defaultValue);
    }

    @Override
    public Integer getInt(String key) {
        return getInt(key, null);
    }

    @Override
    public Boolean getBool(String key, Boolean defaultValue) {
        return Convert.toBool(getStr(key), defaultValue);
    }

    @Override
    public Boolean getBool(String key) {
        return getBool(key, null);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return Convert.toLong(getStr(key), defaultValue);
    }

    @Override
    public Long getLong(String key) {
        return getLong(key, null);
    }

    @Override
    public Character getChar(String key, Character defaultValue) {
        final String value = getStr(key);
        if (StrUtil.isBlank(value)) {
            return defaultValue;
        }
        return value.charAt(0);
    }

    @Override
    public Character getChar(String key) {
        return getChar(key, null);
    }

    @Override
    public Float getFloat(String key) {
        return getFloat(key, null);
    }

    @Override
    public Float getFloat(String key, Float defaultValue) {
        return Convert.toFloat(getStr(key), defaultValue);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) throws NumberFormatException {
        return Convert.toDouble(getStr(key), defaultValue);
    }

    @Override
    public Double getDouble(String key) throws NumberFormatException {
        return getDouble(key, null);
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return Convert.toShort(getStr(key), defaultValue);
    }

    @Override
    public Short getShort(String key) {
        return getShort(key, null);
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return Convert.toByte(getStr(key), defaultValue);
    }

    @Override
    public Byte getByte(String key) {
        return getByte(key, null);
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        final String valueStr = getStr(key);
        if (StrUtil.isBlank(valueStr)) {
            return defaultValue;
        }

        try {
            return new BigDecimal(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public BigDecimal getBigDecimal(String key) {
        return getBigDecimal(key, null);
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger defaultValue) {
        final String valueStr = getStr(key);
        if (StrUtil.isBlank(valueStr)) {
            return defaultValue;
        }

        try {
            return new BigInteger(valueStr);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public BigInteger getBigInteger(String key) {
        return getBigInteger(key, null);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> clazz, String key, E defaultValue) {
        return Convert.toEnum(clazz, getStr(key), defaultValue);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
        return getEnum(clazz, key, null);
    }

    @Override
    public Date getDate(String key, Date defaultValue) {
        return Convert.toDate(getStr(key), defaultValue);
    }

    @Override
    public Date getDate(String key) {
        return getDate(key, null);
    }

    public <T> T getType(String key, Class<T> type) {
        return Convert.convert(type, getObj(key));
    }
}

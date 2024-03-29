package com.gitee.usl.infra.structure;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.getter.BasicTypeGetter;
import cn.hutool.core.getter.OptBasicTypeGetter;
import cn.hutool.core.util.StrUtil;
import com.gitee.usl.infra.constant.NumberConstant;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static cn.hutool.core.map.MapUtil.DEFAULT_LOAD_FACTOR;

/**
 * String类型作为唯一键的 Map，并扩展多种快速转换返回值方法
 *
 * @author hongda.li
 */
public class StringMap<V> extends LinkedHashMap<String, V> implements BasicTypeGetter<String>, OptBasicTypeGetter<String> {
    public StringMap() {
        this(NumberConstant.COMMON_SIZE);
    }

    public StringMap(int size) {
        super(size);
    }

    public StringMap(Map<String, V> otherMap) {
        this((int) (otherMap.size() / DEFAULT_LOAD_FACTOR) + 1);
        this.putAll(otherMap);
    }

    public StringMap<V> putAny(Object key, V value) {
        this.put(String.valueOf(key), value);
        return this;
    }

    public StringMap<V> putOne(String key, V value) {
        this.put(key, value);
        return this;
    }

    @Override
    public Object getObj(String key, Object defaultValue) {
        if (key == null) {
            return defaultValue;
        }
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
        return Convert.toInt(getObj(key), defaultValue);
    }

    @Override
    public Integer getInt(String key) {
        return getInt(key, null);
    }

    @Override
    public Boolean getBool(String key, Boolean defaultValue) {
        return Convert.toBool(getObj(key), defaultValue);
    }

    @Override
    public Boolean getBool(String key) {
        return getBool(key, null);
    }

    @Override
    public Long getLong(String key, Long defaultValue) {
        return Convert.toLong(getObj(key), defaultValue);
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
        return Convert.toFloat(getObj(key), defaultValue);
    }

    @Override
    public Double getDouble(String key, Double defaultValue) throws NumberFormatException {
        return Convert.toDouble(getObj(key), defaultValue);
    }

    @Override
    public Double getDouble(String key) throws NumberFormatException {
        return getDouble(key, null);
    }

    @Override
    public Short getShort(String key, Short defaultValue) {
        return Convert.toShort(getObj(key), defaultValue);
    }

    @Override
    public Short getShort(String key) {
        return getShort(key, null);
    }

    @Override
    public Byte getByte(String key, Byte defaultValue) {
        return Convert.toByte(getObj(key), defaultValue);
    }

    @Override
    public Byte getByte(String key) {
        return getByte(key, null);
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
        try {
            return new BigDecimal(String.valueOf(getObj(key)));
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
        try {
            return new BigInteger(String.valueOf(getObj(key)));
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
        return Convert.toEnum(clazz, getObj(key), defaultValue);
    }

    @Override
    public <E extends Enum<E>> E getEnum(Class<E> clazz, String key) {
        return getEnum(clazz, key, null);
    }

    @Override
    public Date getDate(String key, Date defaultValue) {
        return Convert.toDate(getObj(key), defaultValue);
    }

    @Override
    public Date getDate(String key) {
        return getDate(key, null);
    }

    public <T> T getType(String key, Class<T> type) {
        return Convert.convert(type, getObj(key));
    }
}

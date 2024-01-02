package com.gitee.usl.infra.structure;

import com.gitee.usl.api.annotation.Description;

import java.util.function.BiFunction;

/**
 * @author hongda.li
 */
@Description("元数据属性")
public class AttributeMeta extends StringMap<Object> {

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object replace(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        throw new UnsupportedOperationException();
    }

}

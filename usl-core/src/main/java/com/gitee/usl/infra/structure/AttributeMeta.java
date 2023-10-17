package com.gitee.usl.infra.structure;

import cn.hutool.core.convert.Convert;
import com.gitee.usl.infra.constant.NumberConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * 元数据属性
 *
 * @author hongda.li
 */
public class AttributeMeta {
    private final Map<String, Object> container;

    public AttributeMeta() {
        this.container = new HashMap<>(NumberConstant.EIGHT);
    }

    public void insert(String name, Object value) {
        this.container.put(name, value);
    }

    public Object search(String name) {
        return this.container.get(name);
    }

    public <T> T search(String name, Class<T> type) {
        return Convert.convert(type, this.search(name));
    }

    public void insertAll(Map<String, Object> data) {
        this.container.putAll(data);
    }

    public Map<String, Object> asMap() {
        return new HashMap<>(container);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AttributeMeta.class.getSimpleName() + "[", "]")
                .add("container=" + container)
                .toString();
    }
}

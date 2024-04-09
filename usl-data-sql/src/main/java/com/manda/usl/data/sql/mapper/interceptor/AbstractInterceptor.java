package com.manda.usl.data.sql.mapper.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ModifierUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.db.Entity;
import com.manda.usl.data.sql.BaseMapper;
import com.manda.usl.data.sql.entity.Metadata;
import com.manda.usl.data.sql.mapper.SqlInterceptor;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * @author hongda.li
 */
public abstract class AbstractInterceptor implements SqlInterceptor {
    @Override
    public int order() {
        return 0;
    }

    @Override
    public boolean filter(Class<? extends BaseMapper<?>> mapper) {
        return true;
    }

    protected Object setPrimaryKey(Metadata metadata, Object bean, Long primaryKey) {
        Optional.ofNullable(metadata.getPrimaryKeyField()).ifPresent(field -> {
            Object converted = Convert.convert(field.getType(), primaryKey);
            ReflectUtil.setFieldValue(bean, field, converted);
        });
        return bean;
    }

    protected Entity fetchNotNullFields(Object bean, Metadata metadata) {
        Entity entity = Entity.create(metadata.getTableName());
        Field[] fields = ReflectUtil.getFields(metadata.getEntity(), this::filterField);

        if (ArrayUtil.isEmpty(fields) || bean == null) {
            return entity;
        }

        for (Field field : fields) {
            String columnName = CharSequenceUtil.toUnderlineCase(field.getName());
            try {
                Object value = ReflectUtil.getFieldValue(bean, field);
                if (value != null) {
                    entity.set(columnName, value);
                }
            } catch (Exception ignored) {
            }
        }

        return entity;
    }

    protected boolean filterField(Field field) {
        return !AnnotationUtil.hasAnnotation(field, Transient.class) && !ModifierUtil.isStatic(field);
    }
}

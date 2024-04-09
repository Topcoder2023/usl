package com.manda.usl.data.sql.mapper;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.manda.usl.data.sql.annotation.PrimaryKey;
import com.manda.usl.data.sql.annotation.Select;
import com.manda.usl.data.sql.annotation.Table;
import com.manda.usl.data.sql.entity.Metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

/**
 * @author hongda.li
 */
public final class MetadataParser {
    private MetadataParser() {
    }

    public static void parseEntity(Metadata metadata, Class<?> entity) {
        Objects.requireNonNull(entity);
        // 解析表信息
        parseTableName(metadata, entity);
        // 解析列信息
        parseColumns(metadata, entity);
    }

    public static void parseMapper(Metadata metadata, Class<?> mapper) {
        Objects.requireNonNull(mapper);

        Method[] methods = ReflectUtil.getMethods(mapper, method -> AnnotationUtil.hasAnnotation(method, Select.class));
        if (ArrayUtil.isEmpty(methods)) {
            return;
        }

        for (Method method : methods) {
            String value = AnnotationUtil.getAnnotationValue(method, Select.class);
            String content;
            if (CharSequenceUtil.endWithIgnoreCase(value, ".sql")) {
                content = ResourceUtil.readUtf8Str(value);
            } else {
                content = value;
            }
            Assert.isTrue(CharSequenceUtil.startWithIgnoreCase(content, "SELECT"), "Illegal select statement : {}", content);
            metadata.getSqlMap().put(method.getName(), content);
        }
    }

    private static void parseTableName(Metadata metadata, Class<?> entity) {
        Table table = AnnotationUtil.getAnnotation(entity, Table.class);
        String tableName = Optional.ofNullable(table)
                .map(Table::value)
                .map(name -> CharSequenceUtil.isEmpty(name) ? null : name)
                .orElse(CharSequenceUtil.toUnderlineCase(entity.getSimpleName()));
        metadata.setTableName(tableName);
    }

    private static void parseColumns(Metadata metadata, Class<?> entity) {
        Field[] fields = ReflectUtil.getFields(entity);
        for (Field field : fields) {
            PrimaryKey primaryKey = AnnotationUtil.getAnnotation(field, PrimaryKey.class);
            if (primaryKey != null) {
                Assert.isNull(metadata.getPrimaryKeyField(), "At most one primary key is allowed in a table : {}", metadata.getTableName());
                metadata.setAutoIncrement(primaryKey.autoIncrement());
                metadata.setPrimaryKeyField(field);
            }
        }
    }
}

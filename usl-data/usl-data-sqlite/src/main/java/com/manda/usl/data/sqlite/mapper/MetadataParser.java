package com.manda.usl.data.sqlite.mapper;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.db.meta.JdbcType;
import com.alibaba.fastjson2.JSONObject;
import com.manda.usl.data.sqlite.annotation.PrimaryKey;
import com.manda.usl.data.sqlite.annotation.Select;
import com.manda.usl.data.sqlite.annotation.Table;
import com.manda.usl.data.sqlite.entity.Metadata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
            metadata.getColumnTypeMap().put(CharSequenceUtil.toUnderlineCase(field.getName()), parseColumnType(field.getType()));
        }
    }

    private static JdbcType parseColumnType(Class<?> type) {
        if (int.class.equals(type)
                || Integer.class.equals(type)
                || byte.class.equals(type)
                || Byte.class.equals(type)
                || boolean.class.equals(type)
                || Boolean.class.equals(type)
                || short.class.equals(type)
                || Short.class.equals(type)
                || long.class.equals(type)
                || Long.class.equals(type)
                || BigInteger.class.equals(type)) {
            return JdbcType.INTEGER;
        } else if (double.class.equals(type)
                || Double.class.equals(type)
                || float.class.equals(type)
                || Float.class.equals(type)
                || BigDecimal.class.equals(type)) {
            return JdbcType.REAL;
        } else if (String.class.equals(type)
                || type.isEnum()
                || LocalDate.class.equals(type)
                || LocalDateTime.class.equals(type)
                || ZonedDateTime.class.equals(type)
                || JSONObject.class.equals(type)) {
            return JdbcType.VARCHAR;
        } else {
            return JdbcType.UNDEFINED;
        }
    }
}

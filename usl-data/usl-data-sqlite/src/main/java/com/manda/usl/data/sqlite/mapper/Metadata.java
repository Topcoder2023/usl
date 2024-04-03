package com.manda.usl.data.sqlite.mapper;

import cn.hutool.db.meta.JdbcType;
import lombok.Data;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hongda.li
 */
@Data
public class Metadata {
    /**
     * 数据源
     */
    private DataSource dataSource;
    /**
     * 实体类
     */
    private Class<?> entity;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 是否自动递增
     */
    private boolean autoIncrement;
    /**
     * 主键设置器
     */
    private Field primaryKeyField;
    /**
     * 拦截器
     */
    private List<SqlInterceptor> interceptors;
    /**
     * 方法与 SQL映射
     */
    private final Map<String, String> sqlMap = new LinkedHashMap<>();
    /**
     * 列名与列类型映射
     */
    private final Map<String, JdbcType> columnTypeMap = new LinkedHashMap<>();
}

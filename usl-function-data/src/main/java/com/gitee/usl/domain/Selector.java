package com.gitee.usl.domain;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.gitee.usl.infra.ConnectHelper;
import com.gitee.usl.infra.DatabaseConstant;
import com.gitee.usl.infra.exception.USLException;
import com.gitee.usl.infra.structure.StringList;
import com.gitee.usl.infra.structure.StringSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
public class Selector {
    private final Db proxy;
    private final String tableName;
    private final StringBuilder sql;
    private final List<Object> params;
    private final String databaseName;
    private final StringSet selectColumns;
    private final StringList whereColumns;

    public static Selector of(String tableName) {
        return new Selector(tableName);
    }

    public static Selector of(String databaseName, String tableName) {
        return new Selector(databaseName, tableName);
    }

    public Selector(String tableName) {
        this(DatabaseConstant.DEFAULT_DATABASE_NAME, tableName);
    }

    public Selector(String databaseName, String tableName) {
        this.tableName = tableName;
        this.databaseName = databaseName;
        this.sql = new StringBuilder();
        this.params = new ArrayList<>();
        this.selectColumns = new StringSet();
        this.whereColumns = new StringList();
        this.proxy = ConnectHelper.connect(databaseName).proxy();
    }

    public Selector select(String column) {
        this.selectColumns.add(column);
        return this;
    }

    public Selector select(String... columns) {
        this.selectColumns.addAll(Arrays.asList(columns));
        return this;
    }

    public Selector selectAll() {
        return select("*");
    }

    public Selector andEquals(String column, Object value) {
        this.whereColumns.add(column + " = ?");
        this.params.add(value);
        return this;
    }

    private String buildSql() {
        if (CollUtil.isEmpty(this.selectColumns)) {
            selectAll();
        }
        return sql.append("SELECT ")
                .append(String.join(", ", selectColumns))
                .append(" FROM ")
                .append(tableName)
                .append(" WHERE ")
                .append(String.join(" AND ", whereColumns))
                .toString();
    }

    public Entity findOne() {
        try {
            return proxy.queryOne(this.buildSql(), params.toArray());
        } catch (SQLException e) {
            log.error("SQL查询异常", e);
            throw new USLException(e);
        }
    }

    public List<Entity> findAll() {
        try {
            return proxy.query(this.buildSql(), params.toArray());
        } catch (SQLException e) {
            log.error("SQL查询异常", e);
            throw new USLException(e);
        }
    }

}

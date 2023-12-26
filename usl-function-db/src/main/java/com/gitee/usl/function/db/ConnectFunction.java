package com.gitee.usl.function.db;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;

import java.sql.SQLException;
import java.util.List;

/**
 * @author hongda.li
 */
@FunctionGroup
public class ConnectFunction {

    @Function("db_query_count")
    public long queryCount(String sql, Object... params) throws SQLException {
        return Db.use().count(sql, params);
    }

    @Function("db_query_one")
    public Entity queryOne(String sql, Object... params) throws SQLException {
        return Db.use().queryOne(sql, params);
    }

    @Function("db_query_list")
    public List<Entity> queryList(String sql, Object... params) throws SQLException {
        return Db.use().query(sql, params);
    }

    @Function("db_insert_one")
    public Entity inertOne(Entity entity) throws SQLException {
        Db.use().insert(entity);
        return entity;
    }

    @Function("db_insert_list")
    public List<Entity> inertList(List<Entity> entityList) throws SQLException {
        Db.use().insert(entityList);
        return entityList;
    }
}

package com.gitee.usl.function.data;

import cn.hutool.db.Db;
import cn.hutool.db.Page;
import cn.hutool.db.PageResult;
import cn.hutool.db.sql.Condition;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.exception.USLExecuteException;
import com.gitee.usl.infra.ConnectHelper;
import com.gitee.usl.plugin.annotation.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import cn.hutool.db.Entity;
import static com.gitee.usl.infra.DatabaseConstant.DEFAULT_DATABASE_NAME;


/**
 * 数据库操作函数
 *
 * @author jingshu.zeng, jiahao.song
 */


@Slf4j
@FunctionGroup
public class DatabaseOperationFunction {

    // 执行SQL语句
    @Function("db_sql_execute")
    public void execute(String sql, Object... params) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.execute(sql, params);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }

    //支持命名占位符的SQL执行
    @Function("db_named_query")
    public void namedQuery(String sql, Map<String, Object> paramMap) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.query(sql, paramMap);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }

    @Function("db_insert")
    public void insert(@NotNull String tableName, Map<String, Object> fieldValues) {
        try {
            Entity entity = Entity.create(tableName);
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                entity.set(entry.getKey(), entry.getValue());
            }
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.insert(entity);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    @Function("db_delete")
    public void delete(@NotNull String tableName, Map<String, Object> fieldValues) {
        try {
            Entity entity = Entity.create(tableName);
            for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
                entity.set(entry.getKey(), entry.getValue());
            }
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.del(entity);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    @Function("db_update")
    public void update(@NotNull String tableName, Map<String, Object> updateValues, Map<String, Object> whereValues) {
        try {
            Entity updateEntity = Entity.create(tableName);
            Entity whereEntity = Entity.create(tableName);
            // 设置更新字段信息
            for (Map.Entry<String, Object> entry : updateValues.entrySet()) {
                updateEntity.set(entry.getKey(), entry.getValue());
            }
            // 设置条件字段信息
            for (Map.Entry<String, Object> entry : whereValues.entrySet()) {
                whereEntity.set(entry.getKey(), entry.getValue());
            }
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            proxy.update(updateEntity, whereEntity);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    //查询全部字段
    @Function("db_find_all")
    public List<Entity> findAll(@NotNull String tableName) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            return proxy.findAll(Entity.create(tableName));
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }


    //条件查询
    @Function("db_find_by_condition")
    public List<Entity> findByCondition(String tableName, Map<String, Object> conditionValues) {
        try {
            Entity conditionEntity = Entity.create(tableName);
            for (Map.Entry<String, Object> entry : conditionValues.entrySet()) {
                conditionEntity.set(entry.getKey(), entry.getValue());
            }
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            return proxy.findAll(conditionEntity);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }

    //模糊查询
    @Function("db_find_like")
    public List<Entity> findLike(String tableName, String columnName, String keyword, Condition.LikeType likeType) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            return proxy.findLike(tableName, columnName, keyword, likeType);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }

    }

    //分页查询
    @Function("db_page")
    public PageResult<Entity> page(String tableName, Map<String, Object> conditionValues, int pageNumber, int pageSize) {
        try {
            //Page对象通过传入页码和每页条目数达到分页目的
            Entity conditionEntity = Entity.create(tableName);
            for (Map.Entry<String, Object> entry : conditionValues.entrySet()) {
                conditionEntity.set(entry.getKey(), entry.getValue());
            }
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            return proxy.page(conditionEntity, new Page(pageNumber, pageSize));
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }

    @Function("db_query")
    public List<Entity> query(String sql, Object... params) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            return proxy.query(sql, params);
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }

    }


    //IN查询
    @Function("db_find_in")
    public List<Entity> findIn(String tableName, String columnName, String values) {
        try {
            Db proxy = ConnectHelper.connect(DEFAULT_DATABASE_NAME).proxy();
            return proxy.findAll(
                    Entity.create(tableName)
                            .set(columnName, values)
            );
        } catch (SQLException e) {
            log.error("SQL执行出现错误", e);
            throw new USLExecuteException(e);
        }
    }




}

package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.transaction.TransactionLevel;
import com.manda.usl.data.sqlite.entity.Metadata;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = BatchInsertInterceptor.class)
public class BatchInsertInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "batchInsert".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();

        List<?> toInsertList = ArrayUtil.get(context.getArgs(), 0);
        if (CollUtil.isEmpty(toInsertList)) {
            context.setResult(toInsertList);
            return true;
        }

        try {
            database.tx(TransactionLevel.REPEATABLE_READ, db -> {
                for (Object toInsert : toInsertList) {
                    Entity condition = this.fetchNotNullFields(toInsert, metadata);
                    Long primaryKey = database.insertForGeneratedKey(condition);
                    this.setPrimaryKey(metadata, toInsert, primaryKey);
                }
            });

            context.setResult(toInsertList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.transaction.TransactionLevel;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = BatchDeleteInterceptor.class)
public class BatchDeleteInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "batchDelete".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        List<?> toDeleteList = ArrayUtil.get(context.getArgs(), 0);
        if (CollUtil.isEmpty(toDeleteList)) {
            context.setResult(toDeleteList);
            return true;
        }

        try {
            context.getDatabase().tx(TransactionLevel.REPEATABLE_READ, db -> {
                for (Object toDelete : toDeleteList) {
                    Entity condition = this.fetchNotNullFields(toDelete, context.getMetadata());
                    Assert.notEmpty(condition, "The delete condition can't' be empty");
                    context.getDatabase().del(condition);
                }
            });

            context.setResult(toDeleteList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

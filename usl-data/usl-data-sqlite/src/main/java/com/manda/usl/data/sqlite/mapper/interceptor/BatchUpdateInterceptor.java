package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.transaction.TransactionLevel;
import com.manda.usl.data.sqlite.mapper.Metadata;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = BatchUpdateInterceptor.class)
public class BatchUpdateInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "batchUpdate".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Metadata metadata = context.getMetadata();

        List<?> toUpdateList = ArrayUtil.get(context.getArgs(), 0);
        if (CollUtil.isEmpty(toUpdateList)) {
            context.setResult(toUpdateList);
            return true;
        }

        Field primaryKeyField = metadata.getPrimaryKeyField();
        Assert.notNull(primaryKeyField, "Tables without primary keys doesn't support updating by primary key");
        String primaryKeyColumn = CharSequenceUtil.toUnderlineCase(primaryKeyField.getName());

        try {
            context.getDatabase().tx(TransactionLevel.REPEATABLE_READ, db -> {
                for (Object toUpdate : toUpdateList) {
                    Entity entity = this.fetchNotNullFields(toUpdate, metadata);
                    Object primaryKeyValue = entity.remove(primaryKeyColumn);
                    context.getDatabase().update(entity, Entity.create(metadata.getTableName()).set(primaryKeyColumn, primaryKeyValue));
                }
            });

            context.setResult(toUpdateList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

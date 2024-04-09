package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.manda.usl.data.sqlite.entity.Metadata;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = DeleteInterceptor.class)
public class DeleteInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "delete".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();

        Object toDelete = ArrayUtil.get(context.getArgs(), 0);
        Entity condition = this.fetchNotNullFields(toDelete, metadata);
        Assert.notEmpty(condition, "The delete condition can't' be empty");

        try {
            database.del(condition);
            context.setResult(toDelete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

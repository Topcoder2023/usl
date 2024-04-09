package com.manda.usl.data.sql.mapper.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import com.manda.usl.data.sql.entity.Metadata;
import com.manda.usl.data.sql.mapper.InterceptorContext;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = InsertInterceptor.class)
public class InsertInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "insert".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();
        Object toInsert = ArrayUtil.get(context.getArgs(), 0);

        try {
            Long primaryKey = database.insertForGeneratedKey(this.fetchNotNullFields(toInsert, metadata));
            context.setResult(this.setPrimaryKey(metadata, toInsert, primaryKey));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

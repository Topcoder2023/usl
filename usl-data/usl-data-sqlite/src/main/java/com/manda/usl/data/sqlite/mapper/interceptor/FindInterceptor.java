package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import com.manda.usl.data.sqlite.mapper.Metadata;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = FindInterceptor.class)
public class FindInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "find".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();
        Object condition = ArrayUtil.get(context.getArgs(), 0);

        try {
            context.setResult(database.find(this.fetchNotNullFields(condition, metadata), metadata.getEntity()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

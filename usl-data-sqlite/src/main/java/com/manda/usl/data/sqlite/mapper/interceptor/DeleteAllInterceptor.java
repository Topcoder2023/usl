package com.manda.usl.data.sqlite.mapper.interceptor;

import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = DeleteAllInterceptor.class)
public class DeleteAllInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "deleteAll".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {

        try {
            context.getDatabase().execute("DELETE FROM " + context.getMetadata().getTableName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

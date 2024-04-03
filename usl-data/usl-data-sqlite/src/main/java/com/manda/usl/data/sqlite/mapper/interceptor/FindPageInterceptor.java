package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Page;
import com.manda.usl.data.sqlite.entity.Metadata;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = FindPageInterceptor.class)
public class FindPageInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "findPage".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();
        Object page = ArrayUtil.get(context.getArgs(), 1);
        Object condition = ArrayUtil.get(context.getArgs(), 0);

        try {
            context.setResult(database.page(this.fetchNotNullFields(condition, metadata), (Page) page));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

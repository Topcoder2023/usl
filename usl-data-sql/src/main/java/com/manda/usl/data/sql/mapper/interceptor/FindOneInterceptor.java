package com.manda.usl.data.sql.mapper.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import com.manda.usl.data.sql.entity.Metadata;
import com.manda.usl.data.sql.mapper.InterceptorContext;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = FindOneInterceptor.class)
public class FindOneInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "findOne".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();
        Object condition = ArrayUtil.get(context.getArgs(), 0);

        try {
            List<?> list = database.find(this.fetchNotNullFields(condition, metadata), metadata.getEntity());
            Assert.isTrue(CollUtil.isEmpty(list) || list.size() == 1, "Too many result found but expect one");
            context.setResult(CollUtil.getFirst(list));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

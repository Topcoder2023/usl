package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import cn.hutool.db.handler.BeanListHandler;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Query;
import com.manda.usl.data.sqlite.entity.Metadata;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = FindByInterceptor.class)
public class FindByInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "findBy".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();

        Query query = ArrayUtil.get(context.getArgs(), 0);
        query.setTableNames(metadata.getTableName());

        Condition[] where = query.getWhere();
        if (ArrayUtil.isNotEmpty(where)) {
            List<Condition> whereList = new ArrayList<>(where.length);
            for (Condition condition : where) {
                condition.setField(CharSequenceUtil.toUnderlineCase(condition.getField()));
                whereList.add(condition);
            }
            query.setWhere(whereList.toArray(Condition[]::new));
        }

        try {
            context.setResult(database.find(query, BeanListHandler.create(metadata.getEntity())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

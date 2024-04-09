package com.manda.usl.data.sql.mapper.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Page;
import cn.hutool.db.handler.BeanListHandler;
import cn.hutool.db.sql.Condition;
import cn.hutool.db.sql.Order;
import cn.hutool.db.sql.Query;
import com.manda.usl.data.sql.entity.Metadata;
import com.manda.usl.data.sql.mapper.ConditionBuilder;
import com.manda.usl.data.sql.mapper.InterceptorContext;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Optional;

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

        Query query = new Query(metadata.getTableName());

        ConditionBuilder builder = ArrayUtil.get(context.getArgs(), 0);
        if (builder != null) {
            Condition[] array = builder.getConditions();
            for (Condition condition : array) {
                condition.setField(CharSequenceUtil.toUnderlineCase(condition.getField()));
            }
            query.setWhere(array);

            Page page = builder.getPage();
            query.setPage(page);

            Order[] orders = builder.getOrders();
            if (ArrayUtil.isNotEmpty(orders)) {
                Page withOrder = Optional.ofNullable(query.getPage()).orElse(new Page(0, Integer.MAX_VALUE));
                withOrder.setOrder(orders);
                query.setPage(withOrder);
            }
        }

        try {
            context.setResult(database.find(query, BeanListHandler.create(metadata.getEntity())));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

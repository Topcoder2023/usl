package com.manda.usl.data.sql.mapper.interceptor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.manda.usl.data.sql.entity.Metadata;
import com.manda.usl.data.sql.mapper.InterceptorContext;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = UpdateInterceptor.class)
public class UpdateInterceptor extends AbstractInterceptor {
    @Override
    public boolean filter(Method method) {
        return "update".equals(method.getName());
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        Db database = context.getDatabase();
        Metadata metadata = context.getMetadata();

        Object toUpdate = ArrayUtil.get(context.getArgs(), 0);
        Entity parsed = this.fetchNotNullFields(toUpdate, metadata);

        Field field = metadata.getPrimaryKeyField();
        Assert.notNull(field, "Tables without primary keys doesn't support updating by primary key");

        String column = CharSequenceUtil.toUnderlineCase(field.getName());
        Object value = parsed.remove(column);
        Entity condition = Entity.create(metadata.getTableName()).set(column, value);

        try {
            database.update(parsed, condition);
            context.setResult(toUpdate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

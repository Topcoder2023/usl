package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.db.Db;
import com.manda.usl.data.sqlite.annotation.Select;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = SelectInterceptor.class)
public class SelectInterceptor extends AbstractInterceptor {

    @Override
    public int order() {
        return Integer.MAX_VALUE - 100;
    }

    @Override
    public boolean filter(Method method) {
        return AnnotationUtil.hasAnnotation(method, Select.class);
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        if (context.getResult() != null || CharSequenceUtil.isBlank(context.getSql())) {
            return true;
        }

        String sql = context.getSql();
        Method method = context.getMethod();
        Db database = context.getDatabase();
        Class<?> returnType = method.getReturnType();

        try {
            if (List.class.equals(returnType)) {
                Type argument = TypeUtil.getTypeArgument(method.getGenericReturnType());
                Class<?> elementType;
                if (argument instanceof ParameterizedType) {
                    elementType = (Class<?>) ((ParameterizedType) argument).getRawType();
                } else {
                    elementType = Object.class;
                }
                context.setResult(database.query(sql, elementType, context.getArgs()));
                return true;
            }

            List<?> result = database.query(sql, returnType, context.getArgs());
            Assert.isTrue(CollUtil.size(result) <= 1, "Too many result found but expect one");
            context.setResult(CollUtil.getFirst((List<?>) result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}

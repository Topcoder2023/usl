package com.manda.usl.data.sqlite.mapper.interceptor;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.db.Entity;
import com.manda.usl.data.sqlite.BaseMapper;
import com.manda.usl.data.sqlite.DynamicMapper;
import com.manda.usl.data.sqlite.entity.DynamicEntity;
import com.manda.usl.kernel.annotation.Singleton;

import java.lang.reflect.Method;

/**
 * @author hongda.li
 */
@Singleton(whenNotExist = DynamicMapperInterceptor.class)
public class DynamicMapperInterceptor extends AbstractInterceptor {
    @Override
    public int order() {
        return Integer.MIN_VALUE + 100;
    }

    @Override
    public boolean filter(Method method) {
        return true;
    }

    @Override
    public boolean filter(Class<? extends BaseMapper<?>> mapper) {
        return DynamicMapper.class.isAssignableFrom(mapper);
    }

    @Override
    public boolean intercept(InterceptorContext context) {
        context.getMetadata().setEntity(Entity.class);

        Object dynamicEntity = ArrayUtil.get(context.getArgs(), 0);
        if (dynamicEntity instanceof DynamicEntity entity) {
            String tableName = (String) entity.get(DynamicEntity.FIELD_TABLE_NAME);
            context.getMetadata().setTableName(tableName);
        }
        return true;
    }
}

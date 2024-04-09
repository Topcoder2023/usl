package com.manda.usl.data.sql.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.TypeUtil;
import cn.hutool.db.Db;
import com.manda.usl.data.sql.entity.Metadata;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;

/**
 * @author hongda.li
 */
@Slf4j
public class SqlExecutor implements InvocationHandler {
    private final Db database;
    private final Metadata metadata;

    public SqlExecutor(Class<?> mapper, DataSource dataSource, List<SqlInterceptor> interceptors) {
        this.database = Db.use(dataSource);
        Class<?> entity = (Class<?>) TypeUtil.getTypeArgument(mapper);
        this.metadata = new Metadata();
        this.metadata.setEntity(entity);
        this.metadata.setDataSource(dataSource);
        // 解析Entity对应的元数据
        MetadataParser.parseEntity(this.metadata, entity);
        // 解析Mapper对应的元数据
        MetadataParser.parseMapper(this.metadata, mapper);
        // 解析拦截器对应的元数据
        this.metadata.setInterceptors(interceptors);
        log.debug("Create mapper proxy for [{}]", mapper.getName());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        String methodName = method.getName();
        String sql = metadata.getSqlMap().get(methodName);

        InterceptorContext context = new InterceptorContext();
        context.setSql(sql);
        context.setArgs(args);
        context.setMethod(method);
        context.setMetadata(this.metadata);
        context.setDatabase(this.database);

        List<SqlInterceptor> interceptors = metadata.getInterceptors()
                .stream()
                .filter(interceptor -> interceptor.filter(method))
                .sorted(Comparator.comparingInt(SqlInterceptor::order))
                .toList();

        if (CollUtil.isNotEmpty(interceptors)) {
            interceptors.forEach(item -> item.intercept(context));
            return context.getResult();
        } else {
            return ReflectUtil.invoke(proxy, method, args);
        }
    }
}

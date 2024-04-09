package com.manda.usl.data.sql.mapper;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.db.sql.SqlLog;
import cn.hutool.log.level.Level;
import com.alibaba.druid.pool.DruidDataSource;
import com.manda.usl.data.sql.BaseMapper;
import com.manda.usl.data.sql.annotation.Mapper;
import com.manda.usl.data.sql.entity.DatabaseConfiguration;
import com.manda.usl.kernel.USLRunner;
import com.manda.usl.kernel.bean.BeanContext;
import com.manda.usl.kernel.bean.BeanId;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hongda.li
 */
@Slf4j
@Getter
public class SqlExecutorFactory {

    private final List<SqlInterceptor> interceptorList;

    private final ClassLoader classLoader = ClassUtil.getClassLoader();

    private final Map<Class<?>, Object> mapperMap = new ConcurrentHashMap<>();

    public SqlExecutorFactory() {
        this(BeanContext.find(SqlInterceptor.class));
    }

    public SqlExecutorFactory(List<SqlInterceptor> interceptorList) {
        SqlLog.INSTANCE.init(true, false, true, Level.DEBUG);
        this.interceptorList = interceptorList;
        this.initMapper();
    }

    private void initMapper() {
        DatabaseConfiguration config = USLRunner.getConfiguration().read("data.database", DatabaseConfiguration.class);
        Optional.ofNullable(config.getMapper())
                .orElse(Collections.singletonList(ClassUtil.getPackage(USLRunner.getMain())))
                .stream()
                .map(ClassUtil::scanPackage)
                .flatMap(Collection::stream)
                .distinct()
                .filter(clz -> AnnotationUtil.hasAnnotation(clz, Mapper.class))
                .filter(ClassUtil::isInterface)
                .forEach(mapper -> {
                    //noinspection unchecked
                    BaseMapper<?> baseMapper = getMapper((Class<? extends BaseMapper<?>>) mapper);
                    BeanContext.register(new BeanId()
                            .setType(mapper)
                            .defaultName(), baseMapper);
                });
    }

    public static DataSource defaultDataSource() {
        DatabaseConfiguration config = USLRunner.getConfiguration().read("data.database", DatabaseConfiguration.class);
        log.debug("Data-Database configuration : {}", config);

        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(config.getUrl());
        druidDataSource.setMaxWait(config.getMaxWait());
        druidDataSource.setMinIdle(config.getMinIdle());
        druidDataSource.setUsername(config.getUsername());
        druidDataSource.setPassword(config.getPassword());
        druidDataSource.setMaxActive(config.getMaxActive());
        druidDataSource.setInitialSize(config.getInitialSize());
        druidDataSource.setTestOnBorrow(config.getTestOnBorrow());
        druidDataSource.setTestOnReturn(config.getTestOnReturn());
        druidDataSource.setTestWhileIdle(config.getTestWhileIdle());

        // 自动创建SQLITE的父级目录
        if (config.getUrl().startsWith("jdbc:sqlite:")) {
            FileUtil.mkParentDirs(CharSequenceUtil.removePrefix(config.getUrl(), "jdbc:sqlite:"));
        }

        return druidDataSource;
    }

    /**
     * 根据 Mapper 的类获取对应的代理实例
     *
     * @param mapper Mapper 的类
     * @param <M>    Mapper 的泛型
     * @return 代理实例
     */
    public <M extends BaseMapper<?>> M getMapper(Class<M> mapper) {
        Object proxy = mapperMap.get(mapper);
        if (proxy == null) {
            List<SqlInterceptor> interceptors = interceptorList.stream()
                    .filter(item -> item.filter(mapper))
                    .sorted(Comparator.comparingInt(SqlInterceptor::order))
                    .toList();
            SqlExecutor executor = new SqlExecutor(mapper, this.matchDataSource(mapper), interceptors);
            mapperMap.put(mapper, Proxy.newProxyInstance(this.classLoader, new Class[]{mapper}, executor));
            return this.getMapper(mapper);
        }
        return mapper.cast(proxy);
    }

    private DataSource matchDataSource(Class<?> clz) {
        String dataSourceName = AnnotationUtil.getAnnotationValue(clz, Mapper.class);
        if (CharSequenceUtil.isEmpty(dataSourceName)) {
            return BeanContext.findFirst(DataSource.class);
        } else {
            return BeanContext.findFirst(DataSource.class, dataSourceName);
        }
    }
}

package com.manda.usl.data.sqlite.mapper;

import com.manda.usl.data.sqlite.BaseMapper;
import com.manda.usl.data.sqlite.mapper.interceptor.InterceptorContext;

import java.lang.reflect.Method;

/**
 * @author hongda.li
 */
public interface SqlInterceptor {
    /**
     * 拦截器顺序
     *
     * @return 顺序
     */
    int order();

    /**
     * 拦截的方法名称
     *
     * @return 方法名称
     */
    boolean filter(Method method);

    /**
     * 执行逻辑
     *
     * @param context 拦截上下文
     * @return 是否继续执行后续逻辑
     */
    boolean intercept(InterceptorContext context);

    /**
     * 拦截的目标 Mapper 类型
     *
     * @return 是否执行拦截
     */
    boolean filter(Class<? extends BaseMapper<?>> mapper);
}

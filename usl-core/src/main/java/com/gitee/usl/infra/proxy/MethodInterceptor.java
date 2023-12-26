package com.gitee.usl.infra.proxy;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Description;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author hongda.li
 */
@Description("动态代理接口")
public abstract class MethodInterceptor<E> implements InvocationHandler {

    @Description("Aviator函数默认方法名")
    protected static final String METHOD_NAME = "call";

    @Description("被代理的目标对象")
    private final Object target;

    @Description("被代理的目标类型")
    private final Class<E> targetType;

    @Description("代理接口")
    protected MethodInterceptor(Class<E> targetType) {
        this.target = null;
        this.targetType = targetType;
    }

    @Description("代理对象")
    protected MethodInterceptor(Object target, Class<E> targetType) {
        this.target = target;
        this.targetType = targetType;
    }

    @Description("过滤出需要代理的指定方法")
    protected boolean filter(Method method) {
        return true;
    }

    @Description("拦截指定方法")
    protected abstract Object intercept(Invocation<E> invocation, Object proxy);

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) {
        if (this.filter(method)) {
            Invocation<E> invocation = new Invocation<>(this.target, this.targetType, method, args);
            return this.intercept(invocation, proxy);
        } else {
            return ReflectUtil.invoke(this.target, method, args);
        }
    }

    @Description("创建动态代理实例")
    @SuppressWarnings("unchecked")
    public E createProxy() {
        return (E) Proxy.newProxyInstance(ClassUtil.getClassLoader(), new Class[]{targetType}, this);
    }

}

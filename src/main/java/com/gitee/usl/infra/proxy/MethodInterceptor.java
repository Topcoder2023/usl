package com.gitee.usl.infra.proxy;

import cn.hutool.core.util.ClassUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * USL 动态代理接口
 * 通过指定需要代理的接口类以及代理逻辑来实现功能扩展
 *
 * @author hongda.li
 */
public abstract class MethodInterceptor<E> implements InvocationHandler {
    /**
     * 被代理的目标对象
     * 若代理的目标是接口，则 target 为空
     */
    private final Object target;

    /**
     * 被代理的目标类型
     * 若代理的是接口，则 targetType 为接口的 class
     * 若代理的是对象，则对象必须实现某个接口
     * 且 targetType 为实现接口列表中的指定接口
     */
    private final Class<E> targetType;

    /**
     * 代理接口
     *
     * @param targetType 接口的类型
     */
    protected MethodInterceptor(Class<E> targetType) {
        this.target = null;
        this.targetType = targetType;
    }

    /**
     * 代理对象
     *
     * @param target     代理对象的实例
     * @param targetType 需代理的接口类型
     */
    protected MethodInterceptor(Object target, Class<E> targetType) {
        this.target = target;
        this.targetType = targetType;
    }

    /**
     * 拦截指定方法
     *
     * @param uslInvocation 拦截信息
     * @param proxy         代理对象
     * @return 实际方法返回值
     */
    protected abstract Object intercept(Invocation<E> uslInvocation, Object proxy);

    @Override
    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation<E> invocation = new Invocation<>(this.target, this.targetType, method, args);
        return this.intercept(invocation, proxy);
    }

    /**
     * 创建动态代理实例
     *
     * @return 动态代理
     */
    @SuppressWarnings("unchecked")
    public E createProxy() {
        ClassLoader classLoader = ClassUtil.getClassLoader();
        return (E) Proxy.newProxyInstance(classLoader, new Class[]{targetType}, this);
    }
}

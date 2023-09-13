package com.gitee.usl.infra.proxy;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * USL 动态代理接口
 * 通过指定需要代理的接口类以及代理逻辑来实现功能扩展
 *
 * @author hongda.li
 */
public interface UslProxy {
    /**
     * 代理目标接口
     * 由于使用的是 JDK 内置的代理机制
     * 因此要求被代理的目标必须实现某接口
     *
     * @return 目标接口的类型
     */
    Class<?> target();

    /**
     * 代理目标接口集合
     * 当代理目标存在多个且预期使用相同切面逻辑实现时
     * 可重写此方法
     *
     * @return 目标接口的类型集合
     */
    default List<Class<?>> targetList() {
        return Collections.singletonList(this.target());
    }

    /**
     * 执行动态代理实际逻辑
     *
     * @param target    被代理的目标接口类
     * @param method    被代理的目标接口的方法
     * @param arguments 被代理的目标接口的方法的参数
     * @return 代理后的实际返回值
     */
    Object doProxy(Class<?> target, Method method, Object[] arguments);
}

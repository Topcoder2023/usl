package com.gitee.usl.infra.proxy;

import cn.hutool.core.util.ReflectUtil;
import com.gitee.usl.api.annotation.Description;

import java.lang.reflect.Method;

/**
 * @author hongda.li
 */
@Description("调用记录类")
public record Invocation<E>(@Description("调用实例") Object target,
                            @Description("调用实例类型") Class<E> targetType,
                            @Description("调用实例方法") Method method,
                            @Description("调用实例方法参数") Object[] args) {

    public Object invoke() {
        return ReflectUtil.invoke(target, method, args);
    }

}

package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {
    /**
     * 参数的名称
     * JVM 会默认抹去参数的名称
     * 因此在不开启额外配置前提下，需要通过注解获取
     *
     * @return 名称
     */
    String value();
}

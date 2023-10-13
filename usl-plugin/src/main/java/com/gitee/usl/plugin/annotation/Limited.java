package com.gitee.usl.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limited {
    /**
     * QPS : 每秒请求数
     *
     * @return 浮点数
     */
    double value();

    /**
     * 超时时间
     *
     * @return 时间
     */
    long timeout() default 1;

    /**
     * 超时时间单位
     *
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}

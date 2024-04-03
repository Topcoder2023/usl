package com.manda.usl.kernel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {
    /**
     * 请求路径
     *
     * @return 路径
     */
    String value();

    /**
     * 请求方法
     *
     * @return 方法
     */
    String method() default "";
}

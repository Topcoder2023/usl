package com.manda.usl.http.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Client {
    /**
     * HTTP-Client 组件的名称
     *
     * @return 组件名称
     */
    String value() default "";
}

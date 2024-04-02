package com.manda.usl.kernel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数标记
 *
 * @author hongda.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Function {
    /**
     * 函数名称
     * 默认取方法的名称
     *
     * @return 函数名称
     */
    String value() default "";
}

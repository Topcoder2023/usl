package com.gitee.usl.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Func {
    /**
     * 函数名称
     * 默认取方法或类的名称并将首字母小写
     * 支持指定多个名称
     *
     * @return 函数名称数组
     */
    String[] value() default {};
}

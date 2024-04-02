package com.manda.usl.kernel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 属性注入
 *
 * @author hongda.li
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {
    /**
     * 属性名称
     * 支持按类型自动注入
     * 支持按类型和名称自动注入
     * 支持使用 {} 表达式获取配置文件的值
     *
     * @return 属性标记
     */
    String value() default "";
}

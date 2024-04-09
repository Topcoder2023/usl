package com.manda.usl.data.sql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    /**
     * 默认为驼峰式实体类的名称转为下划线式名称
     *
     * @return 对应数据库的表名
     */
    String value() default "";
}

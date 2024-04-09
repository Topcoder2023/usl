package com.manda.usl.data.sql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {
    /**
     * 标记脚本资源的相对路径
     *
     * @return 相对路径
     */
    String value();
}

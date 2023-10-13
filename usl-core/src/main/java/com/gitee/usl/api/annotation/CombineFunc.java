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
public @interface CombineFunc {
    /**
     * 默认使用的引擎名称
     */
    String DEFAULT_ENGINE_NAME = "USL Runner-1";

    /**
     * 组合函数的名称
     *
     * @return 函数名称
     */
    String[] value() default {};

    /**
     * 组合函数的内容
     *
     * @return 函数内容
     */
    String content();

    /**
     * 引擎名称
     *
     * @return 引擎名称
     */
    String engineName() default DEFAULT_ENGINE_NAME;
}

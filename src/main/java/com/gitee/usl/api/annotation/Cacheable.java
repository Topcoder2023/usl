package com.gitee.usl.api.annotation;

import cn.hutool.core.date.DateUnit;
import com.gitee.usl.infra.constant.NumberConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hongda.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {
    /**
     * 缓存过期时间
     * 非正数表示永不过期
     *
     * @return 过期时间
     */
    long expired() default NumberConstant.MINUS_ONE;

    /**
     * 缓存过期时间单位
     *
     * @return 时间单位
     */
    DateUnit unit() default DateUnit.SECOND;
}

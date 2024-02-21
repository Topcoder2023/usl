package com.gitee.usl.plugin.annotation;

import com.gitee.usl.infra.constant.NumberConstant;
import com.gitee.usl.plugin.api.CacheKeyGenerator;

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
public @interface Cacheable {
    /**
     * 缓存的最大数量
     *
     * @return 数量
     */
    int maxSize() default Integer.MAX_VALUE;

    /**
     * 缓存过期时间
     * 非正数表示永不过期
     *
     * @return 过期时间
     */
    long expired() default NumberConstant.FIVE;

    /**
     * 缓存过期时间单位
     *
     * @return 时间单位
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * 唯一缓存键生成器
     *
     * @return 唯一缓存键生成器
     */
    Class<? extends CacheKeyGenerator> generator() default CacheKeyGenerator.class;
}

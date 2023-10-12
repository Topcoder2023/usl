package com.gitee.usl.api.annotation;

import com.gitee.usl.api.RetryBuilderFactory;
import com.gitee.usl.infra.constant.NumberConstant;

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
public @interface Retryable {
    /**
     * 重试的次数
     *
     * @return 次数
     */
    int times() default NumberConstant.FIVE;

    /**
     * 每次重试之间的间隔时间
     * 设置为非正数时表示立即执行
     *
     * @return 间隔时间
     */
    long retryTime() default NumberConstant.ONE;

    /**
     * 每次重试之间的间隔时间单位
     * 默认每秒
     *
     * @return 间隔时间单位
     */
    TimeUnit retryUnit() default TimeUnit.SECONDS;

    /**
     * 指定的重试器工厂类
     * 当上述规则不满足时，可以指定自定义的重试器工厂类
     * 支持为不同函数配置不同的重试器
     * 当配置有效重试器时，上述配置均会失效
     *
     * @return 指定类
     */
    Class<? extends RetryBuilderFactory> value() default RetryBuilderFactory.class;
}

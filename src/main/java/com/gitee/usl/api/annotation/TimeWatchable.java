package com.gitee.usl.api.annotation;

import com.gitee.usl.api.TimeWatchListener;
import com.gitee.usl.infra.constant.NumberConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author hongda.li
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeWatchable {
    /**
     * 触发监控的阈值
     * 仅当执行时间大于此值时，才会触发监控
     *
     * @return 监控阈值
     */
    long threshold() default NumberConstant.MINUS_ONE;

    /**
     * 执行时长监控的单位
     *
     * @return 单位
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;

    /**
     * 当触发监控时的监听策略
     * 默认仅记录总执行时长
     *
     * @return 监听策略的类
     */
    Class<? extends TimeWatchListener> listener() default TimeWatchListener.class;
}

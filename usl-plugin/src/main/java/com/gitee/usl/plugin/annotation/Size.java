package com.gitee.usl.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 长度校验注解
 *
 * @author jiahao.song
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {

    /**
     * 确切的长度
     *
     * @return 长度值
     */
    int value() default -1;

    /**
     * 最小长度
     *
     * @return 最小长度值，默认为不限制最小长度
     */
    int min() default -1;  // 默认为-1，表示不限制最小长度

    /**
     * 最大长度
     *
     * @return 最大长度值，默认为不限制最大长度
     */
    int max() default -1;  // 默认为-1，表示不限制最大长度

    /**
     * 错误消息
     *
     * @return 错误消息
     */
    String message() default "函数 [{name}] 的第 [{index}] 个参数长度为 [{value}]，预期长度为 [{expect}]";
}

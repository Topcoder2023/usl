package com.gitee.usl.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 正数校验注解
 *
 * @author jiahao.song
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Pattern {

    /**
     * 正则表达式
     *
     * @return 字符串正则表达式
     */
    String regexp();

    /**
     * 错误消息
     *
     * @return 错误消息
     */
    String message() default "函数 [{name}] 的第 [{index}] 个参数为 [{value}]，不符合格式 [{regex}]";
}

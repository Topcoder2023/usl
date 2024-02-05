package com.gitee.usl.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 非空值校验注解
 *
 * @author jingshu.zeng
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    /**
     * 错误消息
     *
     * @return 错误消息
     */
    String message() default "函数 [{name}] 的第 [{index}] 个参数不能为空";

}
